package com.example.unionsystem.controller;

import com.example.unionsystem.dto.response.ApiResponse;
import com.example.unionsystem.entity.CheckIn;
import com.example.unionsystem.service.CheckInService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/checkin")
public class CheckInController {

    private final CheckInService checkInService;

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @PostMapping
    public ApiResponse<CheckIn> checkIn(@RequestParam Long registrationId,
                                        @RequestParam Integer method,
                                        @RequestParam(required = false) BigDecimal latitude,
                                        @RequestParam(required = false) BigDecimal longitude) {
        CheckIn checkIn = checkInService.checkIn(registrationId, method, latitude, longitude);
        return ApiResponse.success("签到成功", checkIn);
    }

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