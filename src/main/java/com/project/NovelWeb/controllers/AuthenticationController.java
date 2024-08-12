package com.project.NovelWeb.controllers;

import com.project.NovelWeb.mappers.UserResponseMapper;
import com.project.NovelWeb.models.dtos.user.LoginDTO;
import com.project.NovelWeb.models.dtos.user.RegisterDTO;
import com.project.NovelWeb.models.entities.Token;
import com.project.NovelWeb.models.entities.User;
import com.project.NovelWeb.responses.LoginResponse;
import com.project.NovelWeb.responses.ResponseObject;
import com.project.NovelWeb.services.TokenService;
import com.project.NovelWeb.services.UserService;
import com.project.NovelWeb.utils.JwtTokenUtils;
import com.project.NovelWeb.utils.localization.LocalizationUtils;
import com.project.NovelWeb.utils.localization.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final LocalizationUtils localizationUtils;
    private final TokenService tokenService;
    private final JwtTokenUtils jwtTokenUtils;
    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;
    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register(
            @Valid @RequestBody RegisterDTO registerDTO
    ) throws Exception{
        if (!registerDTO.getRetypePassword().equals(registerDTO.getPassword())) {
            return ResponseEntity.badRequest().body(ResponseObject
                    .builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH))
                    .build());
        }
        User user = userService.createUser(registerDTO);
        return ResponseEntity.ok(ResponseObject
                .builder()
                .status(HttpStatus.CREATED)
                .data(UserResponseMapper.fromUser(user))
                .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY))
                .build());
    }
    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(
            @Valid @RequestBody LoginDTO loginDTO,
            HttpServletRequest request
    ) throws Exception{
        //Check info and generate Token
        String token = userService.login(loginDTO);
        String userAgent = request.getHeader("User-Agent");
        User user = userService.getUserDetailsFromToken(token);
        Token jwtToken = tokenService.addToken(user, token, isMobileDevice(userAgent));

        LoginResponse loginResponse = fromUserAndToken(user, jwtToken);

        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", jwtToken.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(expirationRefreshToken)
//                .domain("")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(ResponseObject.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                .data(loginResponse)
                .status(HttpStatus.OK)
                .build());
    }
    private boolean isMobileDevice(String userAgent) {
        return userAgent.toLowerCase().contains("mobile");
    }

    @GetMapping("/refresh")
    public ResponseEntity<ResponseObject> getRefreshToken(
            @CookieValue(name = "refresh_token", defaultValue = "abc") String refresh_token) throws Exception {
        Jwt decodedToken = jwtTokenUtils.checkValidRefreshToken(refresh_token);
        String email = decodedToken.getSubject();
        User user = userService.getUserDetailsFromRefreshToken(refresh_token, email);
        Token token = tokenService.refreshToken(refresh_token, email);

        LoginResponse loginResponse = fromUserAndToken(user, token);

        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", token.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(expirationRefreshToken)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(ResponseObject.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKeys.REFRESH_TOKEN_SUCCESSFULLY))
                        .data(loginResponse)
                        .status(HttpStatus.OK)
                        .build());
    }

    private LoginResponse fromUserAndToken(User user, Token token) {
        return LoginResponse.builder()
                .token(token.getToken())
                .tokenType(token.getTokenType())
                .refreshToken(token.getRefreshToken())
                .username(user.getUsername())
                .role(user.getAuthorities().toString())
                .id(user.getId())
                .build();
    }
}
