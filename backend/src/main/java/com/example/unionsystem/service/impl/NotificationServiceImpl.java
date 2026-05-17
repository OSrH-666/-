package com.example.unionsystem.service.impl;

import com.example.unionsystem.entity.Notification;
import com.example.unionsystem.mapper.NotificationMapper;
import com.example.unionsystem.service.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    @Override
    public void createNotification(Long userId, String title, String content, Integer type, Long relatedId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setRelatedId(relatedId);
        notification.setIsRead(0);
        notification.setCreatedAt(LocalDateTime.now());
        notificationMapper.insert(notification);
    }

    @Override
    public void createNotificationBatch(List<Notification> notifications) {
        notifications.forEach(notification -> {
            notification.setIsRead(0);
            notification.setCreatedAt(LocalDateTime.now());
            notificationMapper.insert(notification);
        });
    }

    @Override
    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationMapper.selectByUserId(userId);
    }

    @Override
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationMapper.selectUnreadByUserId(userId);
    }

    @Override
    public void markAsRead(Long id) {
        notificationMapper.updateReadStatusById(id);
    }

    @Override
    public void markAllAsRead(Long userId) {
        notificationMapper.updateReadStatus(userId, 1);
    }

    @Override
    public void deleteNotification(Long id) {
        notificationMapper.deleteById(id);
    }

    @Override
    public int countUnread(Long userId) {
        return notificationMapper.countUnread(userId);
    }
}
