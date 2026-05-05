package com.example.unionsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("club")
public class Club {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private Integer type;
    
    private String description;
    
    private String logoUrl;
    
    private Integer status;
    
    private Long founderId;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}