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

    @PostMapping
    public ApiResponse<Summary> create(@RequestParam Long activityId,
                                       @RequestParam String content,
                                       @RequestParam(required = false) String attachmentUrl) {
        Summary summary = summaryService.createSummary(activityId, content, attachmentUrl);
        return ApiResponse.success("创建成功", summary);
    }

    @PutMapping("/{id}")
    public ApiResponse<Summary> update(@PathVariable Long id,
                                       @RequestParam String content,
                                       @RequestParam(required = false) String attachmentUrl) {
        Summary summary = summaryService.updateSummary(id, content, attachmentUrl);
        return ApiResponse.success("更新成功", summary);
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

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        Summary summary = summaryService.getById(id);
        if (summary != null) {
            Activity activity = activityService.getById(summary.getActivityId());
            if (activity != null && SecurityUtil.isClubManager() && currentUserId != null) {
                if (!clubMemberService.isClubManager(currentUserId, activity.getClubId())) {
                    throw new RuntimeException("您不是该社团的负责人，无权删除活动总结");
                }
            }
        }
        summaryService.removeById(id);
        return ApiResponse.success("删除成功", null);
    }

    @PutMapping("/{id}/approve")
    public ApiResponse<Summary> approve(@PathVariable Long id) {
        Summary summary = summaryService.approveSummary(id);
        return ApiResponse.success("审核通过", summary);
    }

    @PutMapping("/{id}/reject")
    public ApiResponse<Summary> reject(@PathVariable Long id) {
        Summary summary = summaryService.rejectSummary(id);
        return ApiResponse.success("审核拒绝", summary);
    }
}
