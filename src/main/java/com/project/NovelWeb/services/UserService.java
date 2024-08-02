package com.project.NovelWeb.services;

import com.project.NovelWeb.models.dtos.UserDTO;
import com.project.NovelWeb.models.entities.User;

public interface UserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String email, String password, Long roleId) throws Exception;
}
