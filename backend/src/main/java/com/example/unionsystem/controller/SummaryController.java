package com.example.unionsystem.controller;

import com.example.unionsystem.dto.response.ApiResponse;
import com.example.unionsystem.entity.Summary;
import com.example.unionsystem.service.SummaryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    private final SummaryService summaryService;

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
        summaryService.removeById(id);
        return ApiResponse.success("删除成功", null);
    }
}