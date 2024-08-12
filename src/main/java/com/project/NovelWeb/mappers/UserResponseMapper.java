package com.project.NovelWeb.mappers;

import com.project.NovelWeb.models.entities.User;
import com.project.NovelWeb.responses.UserResponse;

public class UserResponseMapper {
    public static UserResponse fromUser(User user) {
        UserResponse userResponse = UserResponse
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .avatar(user.getAvatar())
                .active(user.isActive())
                .role(user.getRole().getName())
                .build();
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return userResponse;
    }
}
