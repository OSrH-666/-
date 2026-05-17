package com.example.unionsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String username;
    
    private String password;
    
    private String realName;
    
    private String studentId;
    
    private String email;
    
    private String phone;
    
    private String role;
    
    private Integer status;
    
    private String grade;
    
    private String major;
    
    private String className;
    
    private String avatarUrl;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
