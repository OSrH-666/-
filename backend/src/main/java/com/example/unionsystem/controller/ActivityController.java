package com.example.unionsystem.controller;

import com.example.unionsystem.dto.response.ApiResponse;
import com.example.unionsystem.entity.Activity;
import com.example.unionsystem.entity.Club;
import com.example.unionsystem.service.ActivityService;
import com.example.unionsystem.service.ClubMemberService;
import com.example.unionsystem.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    private final ActivityService activityService;
    
    @Autowired
    private ClubMemberService clubMemberService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public ApiResponse<List<Activity>> list(@RequestParam(required = false) Long clubId,
                                            @RequestParam(required = false) Integer status) {
        List<Activity> activities = activityService.listActivities(clubId, status);
        return ApiResponse.success(activities);
    }

    @GetMapping("/{id}")
    public ApiResponse<Activity> getById(@PathVariable Long id) {
        Activity activity = activityService.getById(id);
        return ApiResponse.success(activity);
    }

    @PostMapping
    public ApiResponse<Activity> create(@RequestBody Activity activity) {
        // 权限检查：只有社团负责人或管理员可以创建活动
        if (!SecurityUtil.isAdmin() && !clubMemberService.isClubManager(SecurityUtil.getCurrentUserId(), activity.getClubId())) {
            throw new RuntimeException("您不是该社团的负责人，无权创建活动");
        }
        Activity saved = activityService.createActivity(activity);
        return ApiResponse.success("创建成功", saved);
    }

    @PutMapping("/{id}")
    public ApiResponse<Activity> update(@PathVariable Long id, @RequestBody Activity activity) {
        Activity existing = activityService.getById(id);
        if (existing == null) {
            throw new RuntimeException("活动不存在");
        }
        // 权限检查：只有社团负责人或管理员可以更新活动
        if (!SecurityUtil.isAdmin() && !clubMemberService.isClubManager(SecurityUtil.getCurrentUserId(), existing.getClubId())) {
            throw new RuntimeException("您不是该社团的负责人，无权更新活动");
        }
        activity.setId(id);
        Activity updated = activityService.updateActivity(activity);
        return ApiResponse.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        Activity existing = activityService.getById(id);
        if (existing == null) {
            throw new RuntimeException("活动不存在");
        }
        // 权限检查：只有社团负责人或管理员可以删除活动
        if (!SecurityUtil.isAdmin() && !clubMemberService.isClubManager(SecurityUtil.getCurrentUserId(), existing.getClubId())) {
            throw new RuntimeException("您不是该社团的负责人，无权删除活动");
        }
        activityService.deleteActivity(id);
        return ApiResponse.success("删除成功", null);
    }

    @PutMapping("/{id}/publish")
    public ApiResponse<Activity> publish(@PathVariable Long id) {
        Activity existing = activityService.getById(id);
        if (existing == null) {
            throw new RuntimeException("活动不存在");
        }
        // 权限检查：只有社团负责人或管理员可以发布活动
        if (!SecurityUtil.isAdmin() && !clubMemberService.isClubManager(SecurityUtil.getCurrentUserId(), existing.getClubId())) {
            throw new RuntimeException("您不是该社团的负责人，无权发布活动");
        }
        Activity updated = activityService.publishActivity(id);
        return ApiResponse.success("发布成功", updated);
    }

    @PutMapping("/{id}/end")
    public ApiResponse<Activity> end(@PathVariable Long id) {
        Activity existing = activityService.getById(id);
        if (existing == null) {
            throw new RuntimeException("活动不存在");
        }
        // 权限检查：只有社团负责人或管理员可以结束活动
        if (!SecurityUtil.isAdmin() && !clubMemberService.isClubManager(SecurityUtil.getCurrentUserId(), existing.getClubId())) {
            throw new RuntimeException("您不是该社团的负责人，无权结束活动");
        }
        Activity updated = activityService.endActivity(id);
        return ApiResponse.success("活动已结束", updated);
    }
}
