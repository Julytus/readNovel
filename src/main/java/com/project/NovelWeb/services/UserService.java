package com.project.NovelWeb.services;

import com.project.NovelWeb.exceptions.DataNotFoundException;
import com.project.NovelWeb.exceptions.ExpiredTokenException;
import com.project.NovelWeb.models.dtos.user.RegisterDTO;
import com.project.NovelWeb.models.dtos.user.LoginDTO;
import com.project.NovelWeb.models.dtos.user.UpdateUserDTO;
import com.project.NovelWeb.models.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    User createUser(RegisterDTO registerDTO) throws Exception;
    String login(LoginDTO loginDTO) throws Exception;

    User getUserById(Long id) throws DataNotFoundException;

    User updateUser(Long userId, UpdateUserDTO updateUserDTO) throws DataNotFoundException;

    User getUserDetailsFromToken(String token) throws ExpiredTokenException, DataNotFoundException;

    User getUserDetailsFromRefreshToken(String refreshToken, String email) throws Exception;
    void resetPassword(Long userId, String newPassword) throws DataNotFoundException;
    Page<User> searchUser(String keyword, Pageable pageable);
    User getUserByEmai(String email);

    User updateAvatar(User user, MultipartFile file) throws IOException;
    void blockOrEnable(Long userId, Boolean active) throws DataNotFoundException;
}
