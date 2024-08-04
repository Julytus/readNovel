package com.project.NovelWeb.mappers;

import com.project.NovelWeb.models.entities.User;
import com.project.NovelWeb.responses.UserResponse;

public class UserResponseMapper {
    public static UserResponse fromUser(User user) {
        return UserResponse
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .active(user.isActive())
                .role(user.getRole())
                .build();
    }
}
