package com.example.unionsystem.controller;

import com.example.unionsystem.dto.response.ApiResponse;
import com.example.unionsystem.entity.Activity;
import com.example.unionsystem.entity.Summary;
import com.example.unionsystem.service.ActivityService;
import com.example.unionsystem.service.ClubMemberService;
import com.example.unionsystem.service.SummaryService;
import com.example.unionsystem.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    private final SummaryService summaryService;
    
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private ClubMemberService clubMemberService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    // 创建活动总结
    @PostMapping
    public ApiResponse<Summary> create(@RequestParam Long activityId,
                                       @RequestParam String content,
                                       @RequestParam(required = false) String attachmentUrl) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("请先登录");
        }
        
        // 验证是否是该活动的社团负责人
        Activity activity = activityService.getById(activityId);
        if (activity == null) {
            throw new RuntimeException("活动不存在");
        }
        
        if (!SecurityUtil.isAdmin() && !clubMemberService.isClubManager(currentUserId, activity.getClubId())) {
            throw new RuntimeException("您不是该社团的负责人，无权创建活动总结");
        }
        
        Summary summary = summaryService.createSummary(activityId, content, attachmentUrl);
        return ApiResponse.success("创建成功", summary);
    }

    // 更新活动总结
    @PutMapping("/{id}")
    public ApiResponse<Summary> update(@PathVariable Long id,
                                       @RequestParam String content,
                                       @RequestParam(required = false) String attachmentUrl) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("请先登录");
        }
        
        Summary summary = summaryService.getById(id);
        if (summary == null) {
            throw new RuntimeException("总结不存在");
        }
        
        Activity activity = activityService.getById(summary.getActivityId());
        if (activity != null) {
            if (!SecurityUtil.isAdmin() && !clubMemberService.isClubManager(currentUserId, activity.getClubId())) {
                throw new RuntimeException("您不是该社团的负责人，无权更新活动总结");
            }
        }
        
        Summary updated = summaryService.updateSummary(id, content, attachmentUrl);
        return ApiResponse.success("更新成功", updated);
    }

    @GetMapping("/activity/{activityId}")
    public ApiResponse<Summary> getByActivity(@PathVariable Long activityId) {
        Summary summary = summaryService.findByActivityId(activityId);
        return ApiResponse.success(summary);
    }

    @GetMapping("/{id}")
    public ApiResponse<Summary> getById(@PathVariable Long id) {
        Summary summary = summaryService.getById(id);
        return ApiResponse.success(summary);
    }

    // 删除活动总结
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("请先登录");
        }
        
        Summary summary = summaryService.getById(id);
        if (summary == null) {
            throw new RuntimeException("总结不存在");
        }
        
        Activity activity = activityService.getById(summary.getActivityId());
        if (activity != null) {
            // 只有管理员或该社团负责人可以删除
            if (!SecurityUtil.isAdmin() && !clubMemberService.isClubManager(currentUserId, activity.getClubId())) {
                throw new RuntimeException("您不是该社团的负责人，无权删除活动总结");
            }
        }
        
        summaryService.removeById(id);
        return ApiResponse.success("删除成功", null);
    }

    // 管理员审核通过
    @PutMapping("/{id}/approve")
    public ApiResponse<Summary> approve(@PathVariable Long id) {
        // 只有管理员可以审核
        if (!SecurityUtil.isAdmin()) {
            throw new RuntimeException("只有管理员可以审核活动总结");
        }
        
        Summary summary = summaryService.approveSummary(id);
        return ApiResponse.success("审核通过", summary);
    }

    // 管理员审核拒绝
    @PutMapping("/{id}/reject")
    public ApiResponse<Summary> reject(@PathVariable Long id) {
        // 只有管理员可以审核
        if (!SecurityUtil.isAdmin()) {
            throw new RuntimeException("只有管理员可以审核活动总结");
        }
        
        Summary summary = summaryService.rejectSummary(id);
        return ApiResponse.success("审核拒绝", summary);
    }
}
