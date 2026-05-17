package com.example.unionsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("registration")
public class Registration {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long activityId;
    
    private Long userId;
    
    private Integer status;
    
    private Integer queuePosition;
    
    private LocalDateTime registeredAt;
    
    private LocalDateTime confirmedAt;
    
    private LocalDateTime updatedAt;
}
