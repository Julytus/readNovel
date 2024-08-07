package com.project.NovelWeb.controllers;

import com.project.NovelWeb.exceptions.DataNotFoundException;
import com.project.NovelWeb.exceptions.ExpiredTokenException;
import com.project.NovelWeb.mappers.UserResponseMapper;
import com.project.NovelWeb.models.dtos.novel.UpdateUserDTO;
import com.project.NovelWeb.models.entities.User;
import com.project.NovelWeb.responses.ResponseObject;
import com.project.NovelWeb.responses.UserListResponse;
import com.project.NovelWeb.responses.UserResponse;
import com.project.NovelWeb.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("${api.prefix}/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/all")
    public ResponseEntity<ResponseObject> searchUser(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").ascending()
        );
        Page<UserResponse> userPage = userService.searchUser(keyword, pageRequest)
                .map(UserResponseMapper::fromUser);

        int totalPages = userPage.getTotalPages();
        List<UserResponse> userResponses = userPage.getContent();

        UserListResponse userListResponse = UserListResponse
                .builder()
                .users(userResponses)
                .totalPages(totalPages)
                .build();

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Get user list successfully")
                .status(HttpStatus.OK)
                .data(userListResponse)
                .build());
    }
    @GetMapping("/detail")
    public ResponseEntity<ResponseObject> getUserDetail(
            @RequestHeader("Authorization") String authorizationHeader
    ) throws DataNotFoundException, ExpiredTokenException {
        String extractedToken = authorizationHeader.substring(7);
        User user = userService.getUserDetailsFromToken(extractedToken);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Get user's detail successfully")
                        .data(UserResponseMapper.fromUser(user))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @PutMapping("/detail/{userId}")
    public ResponseEntity<ResponseObject> updateUserDetails(
            @PathVariable Long userId,
            @RequestBody UpdateUserDTO updatedUserDTO,
            @RequestHeader("Authorization") String authorizationHeader
    ) throws Exception{
        String extractedToken = authorizationHeader.substring(7);
        User user = userService.getUserDetailsFromToken(extractedToken);
        // Ensure that the user making the request matches the user being updated
        if (!Objects.equals(user.getId(), userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User updatedUser = userService.updateUser(userId, updatedUserDTO);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Update user detail successfully")
                        .data(UserResponseMapper.fromUser(updatedUser))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @PostMapping("/avatar_upload/{id}")
    public ResponseEntity<UserResponse> updateAvatar(
            @PathVariable("id") Long userId,
            @ModelAttribute("file") MultipartFile file,
            @RequestHeader("Authorization") String authorizationHeader
    ) throws Exception {
        String extractedToken = authorizationHeader.substring(7);
        User user = userService.getUserDetailsFromToken(extractedToken);
        // Ensure that the user making the request matches the user being updated
        if (!Objects.equals(user.getId(), userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        user = userService.updateAvatar(user, file);
        return ResponseEntity.ok(UserResponseMapper.fromUser(user));
    }
}
