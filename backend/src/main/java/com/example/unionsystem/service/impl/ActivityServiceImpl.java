package com.example.unionsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.unionsystem.entity.Activity;
import com.example.unionsystem.mapper.ActivityMapper;
import com.example.unionsystem.service.ActivityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {

    @Override
    public List<Activity> listActivities(Long clubId, Integer status) {
        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();
        if (clubId != null) {
            queryWrapper.eq(Activity::getClubId, clubId);
        }
        if (status != null) {
            queryWrapper.eq(Activity::getStatus, status);
        }
        queryWrapper.orderByDesc(Activity::getStartTime);
        return list(queryWrapper);
    }

    @Override
    public Activity createActivity(Activity activity) {
        activity.setStatus(0);
        save(activity);
        return activity;
    }

    @Override
    public Activity updateActivity(Activity activity) {
        updateById(activity);
        return getById(activity.getId());
    }

    @Override
    public void deleteActivity(Long id) {
        removeById(id);
    }

    @Override
    public Activity publishActivity(Long id) {
        Activity activity = getById(id);
        if (activity != null) {
            activity.setStatus(1);
            updateById(activity);
        }
        return activity;
    }

    @Override
    public Activity endActivity(Long id) {
        Activity activity = getById(id);
        if (activity != null) {
            activity.setStatus(2);
            updateById(activity);
        }
        return activity;
    }
}