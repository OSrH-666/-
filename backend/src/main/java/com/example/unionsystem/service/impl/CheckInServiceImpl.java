package com.example.unionsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.unionsystem.entity.CheckIn;
import com.example.unionsystem.entity.Registration;
import com.example.unionsystem.mapper.CheckInMapper;
import com.example.unionsystem.service.CheckInService;
import com.example.unionsystem.service.RegistrationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CheckInServiceImpl extends ServiceImpl<CheckInMapper, CheckIn> implements CheckInService {

    private final RegistrationService registrationService;

    public CheckInServiceImpl(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Override
    public CheckIn checkIn(Long registrationId, Integer method, BigDecimal latitude, BigDecimal longitude) {
        Registration registration = registrationService.getById(registrationId);
        if (registration == null || registration.getStatus() != 1) {
            throw new RuntimeException("报名记录不存在或未确认");
        }

        if (findByRegistrationId(registrationId) != null) {
            throw new RuntimeException("已签到");
        }

        CheckIn checkIn = new CheckIn();
        checkIn.setRegistrationId(registrationId);
        checkIn.setCheckInTime(LocalDateTime.now());
        checkIn.setMethod(method);
        checkIn.setLatitude(latitude);
        checkIn.setLongitude(longitude);
        save(checkIn);
        return checkIn;
    }

    @Override
    public CheckIn manualCheckIn(Long registrationId) {
        return checkIn(registrationId, 3, null, null);
    }

    @Override
    public CheckIn findByRegistrationId(Long registrationId) {
        return getOne(new LambdaQueryWrapper<CheckIn>()
                .eq(CheckIn::getRegistrationId, registrationId));
    }
}