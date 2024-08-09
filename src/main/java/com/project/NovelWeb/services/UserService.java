package com.project.NovelWeb.services;

import com.project.NovelWeb.exceptions.DataNotFoundException;
import com.project.NovelWeb.exceptions.ExpiredTokenException;
import com.project.NovelWeb.models.dtos.UserDTO;
import com.project.NovelWeb.models.dtos.UserLoginDTO;
import com.project.NovelWeb.models.dtos.UpdateUserDTO;
import com.project.NovelWeb.models.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(UserLoginDTO userLoginDTO) throws Exception;

    User getUserById(Long id) throws DataNotFoundException;

    User updateUser(Long userId, UpdateUserDTO updateUserDTO) throws DataNotFoundException;

    User getUserDetailsFromToken(String token) throws ExpiredTokenException, DataNotFoundException;

    User getUserDetailsFromRefreshToken(String refreshToken) throws Exception;
    void resetPassword(Long userId, String newPassword) throws DataNotFoundException;
    Page<User> searchUser(String keyword, Pageable pageable);
    User getUserByEmai(String email);

    User updateAvatar(User user, MultipartFile file) throws IOException;
    void blockOrEnable(Long userId, Boolean active) throws DataNotFoundException;
}
