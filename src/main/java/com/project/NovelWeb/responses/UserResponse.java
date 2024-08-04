package com.project.NovelWeb.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.NovelWeb.models.entities.Role;
import com.project.NovelWeb.models.entities.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("fullname")
    private String fullName;


    @JsonProperty("is_active")
    private boolean active;

    @JsonProperty("email")
    private String email;

    @JsonProperty("role")
    private Role role;
}
