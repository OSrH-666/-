package com.example.unionsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.unionsystem.entity.Activity;
import com.example.unionsystem.entity.Registration;
import com.example.unionsystem.mapper.ActivityMapper;
import com.example.unionsystem.mapper.RegistrationMapper;
import com.example.unionsystem.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistrationServiceImpl extends ServiceImpl<RegistrationMapper, Registration> implements RegistrationService {

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    @Transactional
    public Registration register(Long activityId, Long userId) {
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null || activity.getStatus() != 1) {
            throw new RuntimeException("活动不存在或未发布");
        }

        LambdaQueryWrapper<Registration> existsQuery = new LambdaQueryWrapper<>();
        existsQuery.eq(Registration::getActivityId, activityId).eq(Registration::getUserId, userId);
        if (exists(existsQuery)) {
            throw new RuntimeException("已经报名过该活动");
        }

        int confirmedCount = getConfirmedCount(activityId);
        Registration registration = new Registration();
        registration.setActivityId(activityId);
        registration.setUserId(userId);
        registration.setRegisteredAt(LocalDateTime.now());

        if (activity.getQuota() == 0 || confirmedCount < activity.getQuota()) {
            registration.setStatus(1);
            registration.setConfirmedAt(LocalDateTime.now());
        } else {
            LambdaQueryWrapper<Registration> queueQuery = new LambdaQueryWrapper<>();
            queueQuery.eq(Registration::getActivityId, activityId).eq(Registration::getStatus, 2);
            long queueCount = count(queueQuery);
            registration.setStatus(2);
            registration.setQueuePosition((int) queueCount + 1);
        }

        save(registration);
        return registration;
    }

    @Override
    @Transactional
    public void cancelRegistration(Long activityId, Long userId) {
        LambdaQueryWrapper<Registration> query = new LambdaQueryWrapper<>();
        query.eq(Registration::getActivityId, activityId).eq(Registration::getUserId, userId);
        Registration registration = getOne(query);

        if (registration != null) {
            registration.setStatus(3);
            updateById(registration);
        }
    }

    @Override
    public List<Registration> listByActivity(Long activityId) {
        LambdaQueryWrapper<Registration> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Registration::getActivityId, activityId);
        wrapper.orderByDesc(Registration::getRegisteredAt);
        return list(wrapper);
    }

    @Override
    public List<Registration> listByUser(Long userId) {
        LambdaQueryWrapper<Registration> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Registration::getUserId, userId);
        wrapper.orderByDesc(Registration::getRegisteredAt);
        return list(wrapper);
    }

    @Override
    public int getConfirmedCount(Long activityId) {
        LambdaQueryWrapper<Registration> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Registration::getActivityId, activityId);
        wrapper.eq(Registration::getStatus, 1);
        return (int) count(wrapper);
    }

    @Override
    public void processWaitingQueue(Long activityId) {
    }
}
