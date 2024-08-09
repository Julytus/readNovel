package com.project.NovelWeb.services.impl;

import com.project.NovelWeb.exceptions.DataNotFoundException;
import com.project.NovelWeb.exceptions.ExpiredTokenException;
import com.project.NovelWeb.models.dtos.user.UpdateUserDTO;
import com.project.NovelWeb.models.dtos.user.RegisterDTO;
import com.project.NovelWeb.models.dtos.user.LoginDTO;
import com.project.NovelWeb.models.entities.Role;
import com.project.NovelWeb.models.entities.Token;
import com.project.NovelWeb.models.entities.User;
import com.project.NovelWeb.repositories.RoleRepository;
import com.project.NovelWeb.repositories.TokenRepository;
import com.project.NovelWeb.repositories.UserRepository;
import com.project.NovelWeb.services.UserService;
import com.project.NovelWeb.utils.FileUploadUtil;
import com.project.NovelWeb.utils.jwt.JwtTokenUtils;
import com.project.NovelWeb.utils.localization.LocalizationUtils;
import com.project.NovelWeb.utils.localization.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final LocalizationUtils localizationUtils;
    private final TokenRepository tokenRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private static final String UPLOADS_FOLDER = "uploads/user_avatars";
    @Override
    public User createUser(RegisterDTO registerDTO) throws Exception {
        String email = registerDTO.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException(localizationUtils.getLocalizedMessage(MessageKeys.EMAIL_ALREADY_EXISTS));
        }

        User newUser = User.builder()
                .fullName(registerDTO.getFullName())
                .active(true)
                .email(registerDTO.getEmail())
                .password(registerDTO.getPassword())
                .role(roleRepository.getRoleByName(Role.USER))
                .build();

        //passwordEncode
        String password = registerDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encodedPassword);

        return userRepository.save(newUser);
    }

    @Override
    public String login(LoginDTO loginDTO) throws Exception {
        User currentUser = getUserByEmai(loginDTO.getEmail());
        if (currentUser == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        if(!currentUser.isActive()) {
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_IS_LOCKED));
        }
        
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(), loginDTO.getPassword());

        //authenticate with Java Spring security
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ///Return Token
        return jwtTokenUtils.generateToken(currentUser);
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
        return userRepository.findByEmail(email);
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
    public User getUserByEmai(String email) {
        return userRepository.findByEmail(email);
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
