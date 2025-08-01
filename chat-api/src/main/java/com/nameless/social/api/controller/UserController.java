package com.nameless.social.api.controller;

import com.nameless.social.api.dto.UserDto;
import com.nameless.social.api.model.UserModel;
import com.nameless.social.api.response.CommonResponse;
import com.nameless.social.api.service.UserService;
import com.nameless.social.core.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public CommonResponse<UserModel> createUser(@RequestBody UserDto userDto) {
        User user = userService.getOrCreateUser(userDto.getSocialId(), userDto.getUsername());
        return CommonResponse.success(toModel(user));
    }

    @GetMapping("/{id}")
    public CommonResponse<UserModel> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return CommonResponse.success(toModel(user));
    }

    private UserModel toModel(User user) {
        return UserModel.builder()
                .id(user.getId())
                .username(user.getUsername())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    // TODO: Add endpoint for user registration/login using AWS Cognito
}
