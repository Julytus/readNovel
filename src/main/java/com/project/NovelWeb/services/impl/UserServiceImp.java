package com.project.NovelWeb.services.impl;

import com.project.NovelWeb.components.JwtTokenUtils;
import com.project.NovelWeb.models.dtos.UserDTO;
import com.project.NovelWeb.exceptions.DataNotFoundException;
import com.project.NovelWeb.exceptions.PermissionDenyException;
import com.project.NovelWeb.models.entities.Role;
import com.project.NovelWeb.models.entities.User;
import com.project.NovelWeb.repositories.RoleRepository;
import com.project.NovelWeb.repositories.UserRepository;
import com.project.NovelWeb.services.UserService;
import com.project.NovelWeb.utils.LocalizationUtils;
import com.project.NovelWeb.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;
    private final LocalizationUtils localizationUtils;
    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        String email = userDTO.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException(localizationUtils.getLocalizedMessage(MessageKeys.EMAIL_ALREADY_EXISTS));
        }
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ROLE_NOT_EXIST)));
        if(role.getName().toUpperCase().equals(Role.ADMIN)) {
            throw new PermissionDenyException(localizationUtils.getLocalizedMessage(MessageKeys.ROLE_NOT_EXIST));
        }

        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .active(true)
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .build();

        newUser.setRole(role);

        //passwordEncode
        String password = userDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encodedPassword);

        return userRepository.save(newUser);
    }

    @Override
    public String login(String email, String password, Long roleId) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.EMAIL_DOES_NOT_EXISTS));
        }
        User existingUser = optionalUser.get();
        //check Pass
        if(!passwordEncoder.matches(password, existingUser.getPassword())) {
            throw new BadCredentialsException(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_PASSWORD));
        }

        //check Role
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if(optionalRole.isEmpty() || !roleId.equals(existingUser.getRole().getId())) {
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ROLE_NOT_EXIST));
        }

        if(!optionalUser.get().isActive()) {
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_IS_LOCKED));
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                email, password,
                existingUser.getAuthorities()
        );

        authenticationManager.authenticate(authenticationToken);
        ///Return Token
        return jwtTokenUtils.generateToken(existingUser);
    }
}
