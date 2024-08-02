package com.project.NovelWeb.responses;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("token")
    private String token;
}
