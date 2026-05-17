package com.example.unionsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.unionsystem.dto.request.ChangePasswordRequest;
import com.example.unionsystem.dto.request.LoginRequest;
import com.example.unionsystem.dto.request.RegisterRequest;
import com.example.unionsystem.dto.request.UpdateProfileRequest;
import com.example.unionsystem.dto.response.LoginResponse;
import com.example.unionsystem.entity.User;

import java.util.List;

public interface UserService extends IService<User> {

    LoginResponse login(LoginRequest request);

    void register(RegisterRequest request);

    User findByUsername(String username);

    List<User> listAllUsers();

    User updateUserRole(Long userId, String role);

    User updateUserStatus(Long userId, Integer status);

    User getUserById(Long userId);

    User updateProfile(Long userId, UpdateProfileRequest request);

    void changePassword(Long userId, ChangePasswordRequest request);
}
