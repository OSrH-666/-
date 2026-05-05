package com.example.unionsystem.controller;

import com.example.unionsystem.dto.response.ApiResponse;
import com.example.unionsystem.entity.Registration;
import com.example.unionsystem.service.RegistrationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ApiResponse<Registration> register(@RequestParam Long activityId, @RequestParam Long userId) {
        Registration registration = registrationService.register(activityId, userId);
        return ApiResponse.success("报名成功", registration);
    }

    @DeleteMapping
    public ApiResponse<Void> cancel(@RequestParam Long activityId, @RequestParam Long userId) {
        registrationService.cancelRegistration(activityId, userId);
        return ApiResponse.success("取消成功", null);
    }

    @GetMapping("/activity/{activityId}")
    public ApiResponse<List<Registration>> listByActivity(@PathVariable Long activityId) {
        List<Registration> registrations = registrationService.listByActivity(activityId);
        return ApiResponse.success(registrations);
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<Registration>> listByUser(@PathVariable Long userId) {
        List<Registration> registrations = registrationService.listByUser(userId);
        return ApiResponse.success(registrations);
    }

    @GetMapping("/activity/{activityId}/count")
    public ApiResponse<Integer> getConfirmedCount(@PathVariable Long activityId) {
        int count = registrationService.getConfirmedCount(activityId);
        return ApiResponse.success(count);
    }
}