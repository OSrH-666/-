package com.example.unionsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.unionsystem.dto.request.ChangePasswordRequest;
import com.example.unionsystem.dto.request.LoginRequest;
import com.example.unionsystem.dto.request.RegisterRequest;
import com.example.unionsystem.dto.request.UpdateProfileRequest;
import com.example.unionsystem.dto.response.LoginResponse;
import com.example.unionsystem.entity.User;
import com.example.unionsystem.mapper.UserMapper;
import com.example.unionsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = findByUsername(request.getUsername());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }
        String token = generateToken(user.getUsername());
        String role = mapRole(user.getRole());
        return new LoginResponse(token, user.getUsername(), user.getRealName(), role);
    }

    @Override
    public void register(RegisterRequest request) {
        if (findByUsername(request.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setStudentId(request.getStudentId());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole("STUDENT");
        user.setStatus(1);
        save(user);
    }

    @Override
    public User findByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return getOne(wrapper);
    }

    @Override
    public List<User> listAllUsers() {
        return list();
    }

    @Override
    public User updateUserRole(Long userId, String role) {
        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        // 验证角色有效性
        if (!"ADMIN".equals(role) && !"CLUB_MANAGER".equals(role) && !"STUDENT".equals(role)) {
            throw new RuntimeException("无效的角色");
        }
        user.setRole(role);
        updateById(user);
        return user;
    }

    @Override
    public User updateUserStatus(Long userId, Integer status) {
        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (status != 0 && status != 1) {
            throw new RuntimeException("无效的状态值");
        }
        user.setStatus(status);
        updateById(user);
        return user;
    }

    @Override
    public User getUserById(Long userId) {
        return getById(userId);
    }

    @Override
    public User updateProfile(Long userId, UpdateProfileRequest request) {
        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (request.getRealName() != null) {
            user.setRealName(request.getRealName());
        }
        if (request.getStudentId() != null) {
            user.setStudentId(request.getStudentId());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getGrade() != null) {
            user.setGrade(request.getGrade());
        }
        if (request.getMajor() != null) {
            user.setMajor(request.getMajor());
        }
        if (request.getClassName() != null) {
            user.setClassName(request.getClassName());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        updateById(user);
        return user;
    }

    @Override
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("旧密码不正确");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        updateById(user);
    }

    private String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    private String mapRole(String role) {
        if (role == null) {
            return "student";
        }
        switch (role) {
            case "ADMIN":
                return "admin";
            case "CLUB_MANAGER":
                return "leader";
            case "STUDENT":
            default:
                return "student";
        }
    }
}
