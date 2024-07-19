package com.project.NovelWeb.services.impl;

import com.project.NovelWeb.components.JwtTokenUtils;
import com.project.NovelWeb.dtos.UserDTO;
import com.project.NovelWeb.exceptions.DataNotFoundException;
import com.project.NovelWeb.exceptions.PermissionDenyException;
import com.project.NovelWeb.models.entity.Role;
import com.project.NovelWeb.models.entity.User;
import com.project.NovelWeb.repositories.RoleRepository;
import com.project.NovelWeb.repositories.UserRepository;
import com.project.NovelWeb.services.UserService;
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
    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        String email = userDTO.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("Email already exists.");
        }
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("ROLES_DOES_NOT_EXISTS."));
        if(role.getName().toUpperCase().equals(Role.ADMIN)) {
            throw new PermissionDenyException("NOT_ALLOWED_TO_REGISTER_FOR_AN_ADMIN_ACCOUNT.");
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
            throw new DataNotFoundException("EMAIL_DOES_NOT_EXISTS.");
        }
        User existingUser = optionalUser.get();
        //check Pass
        if(!passwordEncoder.matches(password, existingUser.getPassword())) {
            throw new BadCredentialsException("WRONG_PASSWORD.");
        }

        //check Role
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if(optionalRole.isEmpty() || !roleId.equals(existingUser.getRole().getId())) {
            throw new DataNotFoundException("ROLE_DOES_NOT_EXISTS");
        }

        if(!optionalUser.get().isActive()) {
            throw new DataNotFoundException("USER_IS_LOCKED");
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
