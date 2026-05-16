package com.example.unionsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.unionsystem.entity.Activity;
import com.example.unionsystem.mapper.ActivityMapper;
import com.example.unionsystem.service.ActivityService;
import com.example.unionsystem.service.ClubMemberService;
import com.example.unionsystem.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {

    @Autowired
    private ClubMemberService clubMemberService;

    @Override
    public List<Activity> listActivities(Long clubId, Integer status) {
        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();
        
        // 如果是社团负责人，只能查看自己管理的社团的活动
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (SecurityUtil.isClubManager() && currentUserId != null) {
            List<Long> managedClubIds = clubMemberService.getManagedClubIds(currentUserId);
            if (clubId != null) {
                // 如果指定了clubId，检查是否是自己管理的
                if (!managedClubIds.contains(clubId)) {
                    // 不是自己管理的，返回空列表
                    return List.of();
                }
                queryWrapper.eq(Activity::getClubId, clubId);
            } else {
                // 没有指定clubId，只查询自己管理的社团的活动
                if (managedClubIds.isEmpty()) {
                    return List.of();
                }
                queryWrapper.in(Activity::getClubId, managedClubIds);
            }
        } else {
            // 管理员或普通用户，按正常条件查询
            if (clubId != null) {
                queryWrapper.eq(Activity::getClubId, clubId);
            }
        }
        
        if (status != null) {
            queryWrapper.eq(Activity::getStatus, status);
        }
        queryWrapper.orderByDesc(Activity::getStartTime);
        return list(queryWrapper);
    }

    @Override
    public Activity createActivity(Activity activity) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (SecurityUtil.isClubManager() && currentUserId != null) {
            // 检查是否是该社团的负责人
            if (!clubMemberService.isClubManager(currentUserId, activity.getClubId())) {
                throw new RuntimeException("您不是该社团的负责人，无权创建活动");
            }
        }
        activity.setStatus(0);
        save(activity);
        return activity;
    }

    @Override
    public Activity updateActivity(Activity activity) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        Activity existingActivity = getById(activity.getId());
        if (existingActivity == null) {
            throw new RuntimeException("活动不存在");
        }
        
        if (SecurityUtil.isClubManager() && currentUserId != null) {
            if (!clubMemberService.isClubManager(currentUserId, existingActivity.getClubId())) {
                throw new RuntimeException("您不是该社团的负责人，无权修改活动");
            }
        }
        updateById(activity);
        return getById(activity.getId());
    }

    @Override
    public void deleteActivity(Long id) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        Activity existingActivity = getById(id);
        if (existingActivity == null) {
            throw new RuntimeException("活动不存在");
        }
        
        if (SecurityUtil.isClubManager() && currentUserId != null) {
            if (!clubMemberService.isClubManager(currentUserId, existingActivity.getClubId())) {
                throw new RuntimeException("您不是该社团的负责人，无权删除活动");
            }
        }
        removeById(id);
    }

    @Override
    public Activity publishActivity(Long id) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        Activity activity = getById(id);
        if (activity == null) {
            throw new RuntimeException("活动不存在");
        }
        
        if (SecurityUtil.isClubManager() && currentUserId != null) {
            if (!clubMemberService.isClubManager(currentUserId, activity.getClubId())) {
                throw new RuntimeException("您不是该社团的负责人，无权发布活动");
            }
        }
        
        if (activity != null) {
            activity.setStatus(1);
            updateById(activity);
        }
        return activity;
    }

    @Override
    public Activity endActivity(Long id) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        Activity activity = getById(id);
        if (activity == null) {
            throw new RuntimeException("活动不存在");
        }
        
        if (SecurityUtil.isClubManager() && currentUserId != null) {
            if (!clubMemberService.isClubManager(currentUserId, activity.getClubId())) {
                throw new RuntimeException("您不是该社团的负责人，无权结束活动");
            }
        }
        
        if (activity != null) {
            activity.setStatus(2);
            updateById(activity);
        }
        return activity;
    }
}