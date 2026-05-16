package com.example.unionsystem.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "123456";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Encoded password for '123456': " + encodedPassword);
        
        String storedPassword = "$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq";
        boolean matches = encoder.matches(rawPassword, storedPassword);
        System.out.println("Matches stored password: " + matches);
    }
}