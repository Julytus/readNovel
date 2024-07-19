package com.project.NovelWeb.services;

import com.project.NovelWeb.dtos.UserDTO;
import com.project.NovelWeb.models.entity.User;

public interface UserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String email, String password, Long roleId) throws Exception;
}
