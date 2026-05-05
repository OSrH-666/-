package com.example.unionsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.unionsystem.entity.Activity;

import java.util.List;

public interface ActivityService extends IService<Activity> {

    List<Activity> listActivities(Long clubId, Integer status);

    Activity createActivity(Activity activity);

    Activity updateActivity(Activity activity);

    void deleteActivity(Long id);

    Activity publishActivity(Long id);

    Activity endActivity(Long id);
}