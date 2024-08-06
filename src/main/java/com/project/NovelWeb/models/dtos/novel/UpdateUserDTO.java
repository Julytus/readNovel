package com.project.NovelWeb.models.dtos.novel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateUserDTO {
    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String newPassword;

    @JsonProperty("retype_password")
    private String retypePassword;

    @JsonProperty("current_password")
    private String currentPassword;

}
