package com.example.unionsystem.controller;

import com.example.unionsystem.dto.request.ChangePasswordRequest;
import com.example.unionsystem.dto.request.UpdateProfileRequest;
import com.example.unionsystem.dto.response.ApiResponse;
import com.example.unionsystem.entity.User;
import com.example.unionsystem.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ApiResponse<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ApiResponse.success(user);
    }

    @PutMapping("/{id}")
    public ApiResponse<User> updateProfile(@PathVariable Long id, @RequestBody UpdateProfileRequest request) {
        User updated = userService.updateProfile(id, request);
        return ApiResponse.success("更新成功", updated);
    }

    @PutMapping("/{id}/password")
    public ApiResponse<Void> changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(id, request);
        return ApiResponse.success("密码修改成功", null);
    }
}
