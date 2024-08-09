package com.project.NovelWeb.models.dtos.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    @JsonProperty("email")
    @NotBlank(message = "Email is required!")
    private String email;

    @JsonProperty("password")
    @NotBlank(message = "password cannot be blank!")
    private String password;

    @Min(value = 1, message = "You must enter role's Id, 1. User, 2. Admin, 3. Poster")
    @JsonProperty("role_id")
    private Long roleId = 1L;

    public boolean isPasswordBlank() {
        return password == null || password.trim().isEmpty();
    }}
