package com.example.unionsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.unionsystem.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
}