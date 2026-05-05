package com.example.unionsystem.controller;

import com.example.unionsystem.dto.response.ApiResponse;
import com.example.unionsystem.entity.Activity;
import com.example.unionsystem.service.ActivityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    private final ActivityService activityService;

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
        Activity saved = activityService.createActivity(activity);
        return ApiResponse.success("创建成功", saved);
    }

    @PutMapping("/{id}")
    public ApiResponse<Activity> update(@PathVariable Long id, @RequestBody Activity activity) {
        activity.setId(id);
        Activity updated = activityService.updateActivity(activity);
        return ApiResponse.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return ApiResponse.success("删除成功", null);
    }

    @PutMapping("/{id}/publish")
    public ApiResponse<Activity> publish(@PathVariable Long id) {
        Activity updated = activityService.publishActivity(id);
        return ApiResponse.success("发布成功", updated);
    }

    @PutMapping("/{id}/end")
    public ApiResponse<Activity> end(@PathVariable Long id) {
        Activity updated = activityService.endActivity(id);
        return ApiResponse.success("活动已结束", updated);
    }
}