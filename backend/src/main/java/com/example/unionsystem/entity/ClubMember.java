package com.example.unionsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("club_member")
public class ClubMember {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long clubId;
    
    private Long userId;
    
    private Integer role;
    
    private Integer status;
    
    private LocalDateTime joinedAt;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
