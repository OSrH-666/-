package com.example.unionsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.unionsystem.entity.Activity;
import com.example.unionsystem.entity.Summary;
import com.example.unionsystem.mapper.SummaryMapper;
import com.example.unionsystem.service.ActivityService;
import com.example.unionsystem.service.SummaryService;
import org.springframework.stereotype.Service;

@Service
public class SummaryServiceImpl extends ServiceImpl<SummaryMapper, Summary> implements SummaryService {

    private final ActivityService activityService;

    public SummaryServiceImpl(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Override
    public Summary createSummary(Long activityId, String content, String attachmentUrl) {
        Activity activity = activityService.getById(activityId);
        if (activity == null) {
            throw new RuntimeException("活动不存在");
        }

        if (findByActivityId(activityId) != null) {
            throw new RuntimeException("该活动已有总结");
        }

        Summary summary = new Summary();
        summary.setActivityId(activityId);
        summary.setContent(content);
        summary.setAttachmentUrl(attachmentUrl);
        save(summary);
        return summary;
    }

    @Override
    public Summary updateSummary(Long id, String content, String attachmentUrl) {
        Summary summary = getById(id);
        if (summary != null) {
            summary.setContent(content);
            summary.setAttachmentUrl(attachmentUrl);
            updateById(summary);
        }
        return summary;
    }

    @Override
    public Summary findByActivityId(Long activityId) {
        return getOne(new LambdaQueryWrapper<Summary>()
                .eq(Summary::getActivityId, activityId));
    }
}