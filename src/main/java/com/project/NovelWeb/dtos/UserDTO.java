package com.project.NovelWeb.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.NovelWeb.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @JsonProperty("full_name")
    private String fullName;

    @NotBlank(message = "Email is required!")
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "Password cannot be blank!")
    @JsonProperty("password")
    private String password;

    @JsonProperty("retype_password")
    private String retypePassword;

    @NotNull(message = "Role Id is required!")
    @JsonProperty("role_id")
    private Long roleId;

}
