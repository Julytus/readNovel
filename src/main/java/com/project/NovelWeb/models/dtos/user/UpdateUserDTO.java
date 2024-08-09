package com.project.NovelWeb.models.dtos.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateUserDTO {
    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("new_password")
    private String newPassword;

    @JsonProperty("retype_password")
    private String retypePassword;

    @JsonProperty("current_password")
    private String currentPassword;

}
