package com.example.unionsystem.mapper;

import com.example.unionsystem.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationMapper {
    int insert(Notification notification);
    int updateById(Notification notification);
    int deleteById(Long id);
    Notification selectById(Long id);
    List<Notification> selectByUserId(Long userId);
    List<Notification> selectUnreadByUserId(Long userId);
    int updateReadStatus(@Param("userId") Long userId, @Param("isRead") Integer isRead);
    int updateReadStatusById(Long id);
    int countUnread(Long userId);
}
