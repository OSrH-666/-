package com.example.unionsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.unionsystem.entity.CheckIn;

import java.math.BigDecimal;

public interface CheckInService extends IService<CheckIn> {

    CheckIn checkIn(Long registrationId, Integer method, BigDecimal latitude, BigDecimal longitude);

    CheckIn manualCheckIn(Long registrationId);

    CheckIn findByRegistrationId(Long registrationId);
}