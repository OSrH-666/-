package com.example.unionsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.unionsystem.entity.Activity;
import com.example.unionsystem.entity.Registration;
import com.example.unionsystem.mapper.RegistrationMapper;
import com.example.unionsystem.service.ActivityService;
import com.example.unionsystem.service.RegistrationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistrationServiceImpl extends ServiceImpl<RegistrationMapper, Registration> implements RegistrationService {

    private final ActivityService activityService;

    public RegistrationServiceImpl(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Override
    @Transactional
    public Registration register(Long activityId, Long userId) {
        Activity activity = activityService.getById(activityId);
        if (activity == null || activity.getStatus() != 1) {
            throw new RuntimeException("活动不存在或未发布");
        }

        LambdaQueryWrapper<Registration> existsQuery = new LambdaQueryWrapper<>();
        existsQuery.eq(Registration::getActivityId, activityId)
                   .eq(Registration::getUserId, userId);
        if (exists(existsQuery)) {
            throw new RuntimeException("已报名该活动");
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
            int queuePosition = (int) count(new LambdaQueryWrapper<Registration>()
                    .eq(Registration::getActivityId, activityId)
                    .eq(Registration::getStatus, 2)) + 1;
            registration.setStatus(2);
            registration.setQueuePosition(queuePosition);
        }

        save(registration);
        return registration;
    }

    @Override
    @Transactional
    public void cancelRegistration(Long activityId, Long userId) {
        LambdaQueryWrapper<Registration> query = new LambdaQueryWrapper<>();
        query.eq(Registration::getActivityId, activityId)
             .eq(Registration::getUserId, userId);
        Registration registration = getOne(query);

        if (registration != null) {
            if (registration.getStatus() == 1) {
                registration.setStatus(3);
                updateById(registration);
                processWaitingQueue(activityId);
            } else if (registration.getStatus() == 2) {
                registration.setStatus(3);
                updateById(registration);
                updateQueuePositions(activityId, registration.getQueuePosition());
            }
        }
    }

    @Override
    public List<Registration> listByActivity(Long activityId) {
        return list(new LambdaQueryWrapper<Registration>()
                .eq(Registration::getActivityId, activityId)
                .orderByDesc(Registration::getRegisteredAt));
    }

    @Override
    public List<Registration> listByUser(Long userId) {
        return list(new LambdaQueryWrapper<Registration>()
                .eq(Registration::getUserId, userId)
                .orderByDesc(Registration::getRegisteredAt));
    }

    @Override
    public int getConfirmedCount(Long activityId) {
        return (int) count(new LambdaQueryWrapper<Registration>()
                .eq(Registration::getActivityId, activityId)
                .eq(Registration::getStatus, 1));
    }

    @Override
    @Transactional
    public void processWaitingQueue(Long activityId) {
        Activity activity = activityService.getById(activityId);
        if (activity == null || activity.getQuota() == 0) {
            return;
        }

        int currentCount = getConfirmedCount(activityId);
        int availableSlots = activity.getQuota() - currentCount;

        if (availableSlots > 0) {
            List<Registration> waitingList = list(new LambdaQueryWrapper<Registration>()
                    .eq(Registration::getActivityId, activityId)
                    .eq(Registration::getStatus, 2)
                    .orderByAsc(Registration::getQueuePosition)
                    .last("LIMIT " + availableSlots));

            for (Registration registration : waitingList) {
                registration.setStatus(1);
                registration.setConfirmedAt(LocalDateTime.now());
                registration.setQueuePosition(0);
                updateById(registration);
            }
        }
    }

    private void updateQueuePositions(Long activityId, int startPosition) {
        List<Registration> waitingList = list(new LambdaQueryWrapper<Registration>()
                .eq(Registration::getActivityId, activityId)
                .eq(Registration::getStatus, 2)
                .gt(Registration::getQueuePosition, startPosition)
                .orderByAsc(Registration::getQueuePosition));

        for (Registration registration : waitingList) {
            registration.setQueuePosition(registration.getQueuePosition() - 1);
            updateById(registration);
        }
    }
}