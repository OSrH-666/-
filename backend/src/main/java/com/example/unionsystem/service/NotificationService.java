package com.example.unionsystem.service;

import com.example.unionsystem.entity.Notification;

import java.util.List;

public interface NotificationService {
    void createNotification(Long userId, String title, String content, Integer type, Long relatedId);
    void createNotificationBatch(List<Notification> notifications);
    List<Notification> getNotificationsByUserId(Long userId);
    List<Notification> getUnreadNotifications(Long userId);
    void markAsRead(Long id);
    void markAllAsRead(Long userId);
    void deleteNotification(Long id);
    int countUnread(Long userId);
}
