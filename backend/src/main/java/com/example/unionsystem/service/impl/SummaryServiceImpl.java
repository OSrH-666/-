package com.example.unionsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.unionsystem.entity.Activity;
import com.example.unionsystem.entity.Summary;
import com.example.unionsystem.mapper.SummaryMapper;
import com.example.unionsystem.service.ActivityService;
import com.example.unionsystem.service.ClubMemberService;
import com.example.unionsystem.service.SummaryService;
import com.example.unionsystem.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SummaryServiceImpl extends ServiceImpl<SummaryMapper, Summary> implements SummaryService {

    private final ActivityService activityService;

    @Autowired
    private ClubMemberService clubMemberService;

    public SummaryServiceImpl(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Override
    public Summary createSummary(Long activityId, String content, String attachmentUrl) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        Activity activity = activityService.getById(activityId);
        if (activity == null) {
            throw new RuntimeException("活动不存在");
        }

        if (SecurityUtil.isClubManager() && currentUserId != null) {
            if (!clubMemberService.isClubManager(currentUserId, activity.getClubId())) {
                throw new RuntimeException("您不是该社团的负责人，无权创建活动总结");
            }
        }

        if (findByActivityId(activityId) != null) {
            throw new RuntimeException("该活动已有总结");
        }

        Summary summary = new Summary();
        summary.setActivityId(activityId);
        summary.setContent(content);
        summary.setAttachmentUrl(attachmentUrl);
        summary.setStatus(0); // 默认为待审核
        save(summary);
        return summary;
    }

    @Override
    public Summary updateSummary(Long id, String content, String attachmentUrl) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        Summary summary = getById(id);
        if (summary == null) {
            throw new RuntimeException("总结不存在");
        }

        Activity activity = activityService.getById(summary.getActivityId());
        if (activity != null && SecurityUtil.isClubManager() && currentUserId != null) {
            if (!clubMemberService.isClubManager(currentUserId, activity.getClubId())) {
                throw new RuntimeException("您不是该社团的负责人，无权修改活动总结");
            }
        }

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

    @Override
    public Summary approveSummary(Long id) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        Summary summary = getById(id);
        if (summary == null) {
            throw new RuntimeException("总结不存在");
        }

        Activity activity = activityService.getById(summary.getActivityId());
        if (activity != null && SecurityUtil.isClubManager() && currentUserId != null) {
            if (!clubMemberService.isClubManager(currentUserId, activity.getClubId())) {
                throw new RuntimeException("您不是该社团的负责人，无权审核活动总结");
            }
        }

        summary.setStatus(1); // 审核通过
        updateById(summary);
        return summary;
    }

    @Override
    public Summary rejectSummary(Long id) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        Summary summary = getById(id);
        if (summary == null) {
            throw new RuntimeException("总结不存在");
        }

        Activity activity = activityService.getById(summary.getActivityId());
        if (activity != null && SecurityUtil.isClubManager() && currentUserId != null) {
            if (!clubMemberService.isClubManager(currentUserId, activity.getClubId())) {
                throw new RuntimeException("您不是该社团的负责人，无权审核活动总结");
            }
        }

        summary.setStatus(2); // 审核拒绝
        updateById(summary);
        return summary;
    }
}
