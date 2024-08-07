package com.project.NovelWeb.services.impl;

import com.project.NovelWeb.exceptions.ExpiredTokenException;
import com.project.NovelWeb.models.dtos.UserLoginDTO;
import com.project.NovelWeb.models.dtos.novel.UpdateUserDTO;
import com.project.NovelWeb.models.entities.Token;
import com.project.NovelWeb.repositories.TokenRepository;
import com.project.NovelWeb.utils.FileUploadUtil;
import com.project.NovelWeb.utils.jwt.JwtTokenUtils;
import com.project.NovelWeb.models.dtos.UserDTO;
import com.project.NovelWeb.exceptions.DataNotFoundException;
import com.project.NovelWeb.exceptions.PermissionDenyException;
import com.project.NovelWeb.models.entities.Role;
import com.project.NovelWeb.models.entities.User;
import com.project.NovelWeb.repositories.RoleRepository;
import com.project.NovelWeb.repositories.UserRepository;
import com.project.NovelWeb.services.UserService;
import com.project.NovelWeb.utils.localization.LocalizationUtils;
import com.project.NovelWeb.utils.localization.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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
    private final TokenRepository tokenRepository;
    private static final String UPLOADS_FOLDER = "uploads/user_avatars";
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
    public String login(UserLoginDTO userLoginDTO) throws Exception {
        Optional<User> optionalUser = Optional.empty();
        String subject = null;

        //CheckGmail,Fb...
        //Code

        if (userLoginDTO.getEmail() != null && !userLoginDTO.getEmail().isBlank()) {
            optionalUser = userRepository.findByEmail(userLoginDTO.getEmail());
            subject = userLoginDTO.getEmail();
        }
        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.EMAIL_DOES_NOT_EXISTS));
        }

        User existingUser = optionalUser.get();
        // Check if the user account is active
        if(!optionalUser.get().isActive()) {
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_IS_LOCKED));
        }

        //check Pass
        if(!passwordEncoder.matches(userLoginDTO.getPassword(), existingUser.getPassword())) {
            throw new BadCredentialsException(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_PASSWORD));
        }

        //check Role
        Optional<Role> optionalRole = roleRepository.findById(userLoginDTO.getRoleId());
        if(optionalRole.isEmpty() || !userLoginDTO.getRoleId().equals(existingUser.getRole().getId())) {
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ROLE_NOT_EXIST));
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                subject,
                userLoginDTO.isPasswordBlank()  ? "" : userLoginDTO.getPassword(),
                existingUser.getAuthorities()
        );
        //authenticate with Java Spring security
        authenticationManager.authenticate(authenticationToken);
        ///Return Token
        return jwtTokenUtils.generateToken(existingUser);
    }

    @Override
    public User getUserById(Long id) throws DataNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
    }

    @Override
    public User updateUser(Long userId, UpdateUserDTO updateUserDTO) throws DataNotFoundException {
        User existingUser = getUserById(userId);

        //Check current Password
        if(!passwordEncoder.matches(updateUserDTO.getCurrentPassword(), existingUser.getPassword())) {
            throw new BadCredentialsException(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_PASSWORD));
        }
        //Check email already exists
        String newEmail = updateUserDTO.getEmail();
        if (!existingUser.getEmail().equals(newEmail) &&
                userRepository.existsByEmail(newEmail)) {
            throw new DataIntegrityViolationException("Email already exists");
        }
        //Check retype password => Encode password => Save the updated user
        if (updateUserDTO.getNewPassword() != null
                && !updateUserDTO.getNewPassword().isEmpty()) {
            if(!updateUserDTO.getNewPassword().equals(updateUserDTO.getRetypePassword())) {
                throw new BadCredentialsException("Password and retype password not the same");
            }
            String newPassword = updateUserDTO.getNewPassword();
            String encodedPassword = passwordEncoder.encode(newPassword);
            existingUser.setPassword(encodedPassword);
        }
        return userRepository.save(existingUser);
    }

    @Override
    public User getUserDetailsFromToken(String token) throws ExpiredTokenException, DataNotFoundException {
        if(jwtTokenUtils.isTokenExpired(token)) {
            throw new ExpiredTokenException("Token is expired");
        }
        String email = jwtTokenUtils.extractEmail(token);
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new DataNotFoundException("User not found");
        }
    }
    @Override
    public User getUserDetailsFromRefreshToken(String refreshToken) throws Exception {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken);
        return getUserDetailsFromToken(existingToken.getToken());
    }

    @Override
    @Transactional
    public void resetPassword(Long userId, String newPassword) throws DataNotFoundException {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        String encodedPassword = passwordEncoder.encode(newPassword);
        existingUser.setPassword(encodedPassword);
        userRepository.save(existingUser);
        //reset password => clear token
        List<Token> tokens = tokenRepository.findByUser(existingUser);
        tokenRepository.deleteAll(tokens);
    }

    @Override
    public Page<User> searchUser(String keyword, Pageable pageable) {
        return userRepository.searchUser(keyword, pageable);
    }

    @Override
    public User updateAvatar(User user, MultipartFile file) throws IOException {
        FileUploadUtil.updateImage(user, file, UPLOADS_FOLDER, user.getId());
        return userRepository.save(user);
    }
    @Override
    @Transactional
    public void blockOrEnable(Long userId, Boolean active) throws DataNotFoundException {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        existingUser.setActive(active);
        userRepository.save(existingUser);
    }
}
