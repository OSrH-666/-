package com.example.unionsystem.controller;

import com.example.unionsystem.dto.response.ApiResponse;
import com.example.unionsystem.entity.CheckIn;
import com.example.unionsystem.entity.Registration;
import com.example.unionsystem.service.CheckInService;
import com.example.unionsystem.service.RegistrationService;
import com.example.unionsystem.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/checkin")
public class CheckInController {

    private final CheckInService checkInService;
    
    @Autowired
    private RegistrationService registrationService;

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    // 学生签到
    @PostMapping
    public ApiResponse<CheckIn> checkIn(@RequestParam Long registrationId,
                                        @RequestParam Integer method,
                                        @RequestParam(required = false) BigDecimal latitude,
                                        @RequestParam(required = false) BigDecimal longitude) {
        // 验证登录
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("请先登录");
        }
        
        // 验证报名记录是否存在且属于当前用户
        Registration registration = registrationService.getById(registrationId);
        if (registration == null) {
            throw new RuntimeException("报名记录不存在");
        }
        if (!registration.getUserId().equals(currentUserId)) {
            throw new RuntimeException("您无权为他人签到");
        }
        
        // 验证报名状态
        if (registration.getStatus() != 1) {
            throw new RuntimeException("您的报名状态不允许签到");
        }
        
        CheckIn checkIn = checkInService.checkIn(registrationId, method, latitude, longitude);
        return ApiResponse.success("签到成功", checkIn);
    }

    // 手动签到（社团负责人操作）
    @PostMapping("/manual")
    public ApiResponse<CheckIn> manualCheckIn(@RequestParam Long registrationId) {
        CheckIn checkIn = checkInService.manualCheckIn(registrationId);
        return ApiResponse.success("签到成功", checkIn);
    }

    @GetMapping("/registration/{registrationId}")
    public ApiResponse<CheckIn> getByRegistration(@PathVariable Long registrationId) {
        CheckIn checkIn = checkInService.findByRegistrationId(registrationId);
        return ApiResponse.success(checkIn);
    }
}
