package com.example.unionsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.unionsystem.entity.Summary;

public interface SummaryService extends IService<Summary> {

    Summary createSummary(Long activityId, String content, String attachmentUrl);

    Summary updateSummary(Long id, String content, String attachmentUrl);

    Summary findByActivityId(Long activityId);
}