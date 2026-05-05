package com.example.unionsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.unionsystem.entity.Registration;

import java.util.List;

public interface RegistrationService extends IService<Registration> {

    Registration register(Long activityId, Long userId);

    void cancelRegistration(Long activityId, Long userId);

    List<Registration> listByActivity(Long activityId);

    List<Registration> listByUser(Long userId);

    int getConfirmedCount(Long activityId);

    void processWaitingQueue(Long activityId);
}