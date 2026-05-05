package com.example.unionsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.unionsystem.entity.Registration;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RegistrationMapper extends BaseMapper<Registration> {
}