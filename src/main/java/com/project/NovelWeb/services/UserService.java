package com.project.NovelWeb.services;

import com.project.NovelWeb.exceptions.DataNotFoundException;
import com.project.NovelWeb.exceptions.ExpiredTokenException;
import com.project.NovelWeb.models.dtos.UserDTO;
import com.project.NovelWeb.models.dtos.novel.UpdateUserDTO;
import com.project.NovelWeb.models.entities.User;
import com.project.NovelWeb.responses.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String email, String password, Long roleId) throws Exception;

    User updateUser(Long userId, UpdateUserDTO updateUserDTO) throws DataNotFoundException;

    User getUserDetailsFromToken(String token) throws ExpiredTokenException, DataNotFoundException;

    void resetPassword(Long userId, String newPassword) throws DataNotFoundException;
    Page<User> searchUser(String keyword, Pageable pageable);
}
