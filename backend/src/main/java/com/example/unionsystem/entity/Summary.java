package com.example.unionsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("summary")
public class Summary {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long activityId;
    
    private String content;
    
    private String attachmentUrl;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}