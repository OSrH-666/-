package com.example.unionsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("check_in")
public class CheckIn {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long registrationId;
    
    private LocalDateTime checkInTime;
    
    private Integer method;
    
    private BigDecimal latitude;
    
    private BigDecimal longitude;
    
    private LocalDateTime createdAt;
}
