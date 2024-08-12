package com.project.NovelWeb.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse extends BaseResponse{
    @JsonProperty("id")
    private Long id;

    @JsonProperty("full_name")
    private String fullName;


    @JsonProperty("is_active")
    private boolean active;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("email")
    private String email;

    @JsonProperty("role")
    private String role;

}
