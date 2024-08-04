package com.project.NovelWeb.controllers;

import com.project.NovelWeb.mappers.UserResponseMapper;
import com.project.NovelWeb.models.dtos.UserDTO;
import com.project.NovelWeb.models.dtos.UserLoginDTO;
import com.project.NovelWeb.exceptions.MethodArgumentNotValidException;
import com.project.NovelWeb.models.entities.User;
import com.project.NovelWeb.responses.LoginResponse;
import com.project.NovelWeb.responses.ResponseObject;
import com.project.NovelWeb.services.UserService;
import com.project.NovelWeb.utils.localization.LocalizationUtils;
import com.project.NovelWeb.utils.localization.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult bindingResult
    ) throws Exception{
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(bindingResult);
        }

        if (!userDTO.getRetypePassword().equals(userDTO.getPassword())) {
            return ResponseEntity.badRequest().body(ResponseObject
                    .builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH))
                    .build());
        }
        User user = userService.createUser(userDTO);
        return ResponseEntity.ok(ResponseObject
                .builder()
                .status(HttpStatus.CREATED)
                .data(UserResponseMapper.fromUser(user))
                .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY))
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO
    ) throws Exception{
        //Check info and generate Token
        String token = userService.login(
                userLoginDTO.getEmail(),
                userLoginDTO.getPassword(),
                userLoginDTO.getRoleId() == null ? 1 : userLoginDTO.getRoleId());
        return ResponseEntity.ok(LoginResponse
                .builder()
                .token(token)
                .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                .build());
    }
}
