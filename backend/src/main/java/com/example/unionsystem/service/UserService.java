package com.example.unionsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.unionsystem.dto.request.LoginRequest;
import com.example.unionsystem.dto.request.RegisterRequest;
import com.example.unionsystem.dto.response.LoginResponse;
import com.example.unionsystem.entity.User;

public interface UserService extends IService<User> {

    LoginResponse login(LoginRequest request);

    void register(RegisterRequest request);

    User findByUsername(String username);
}