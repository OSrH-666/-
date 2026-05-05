package com.example.unionsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.unionsystem.entity.Club;
import com.example.unionsystem.mapper.ClubMapper;
import com.example.unionsystem.service.ClubService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubServiceImpl extends ServiceImpl<ClubMapper, Club> implements ClubService {

    @Override
    public List<Club> listClubs(Integer status) {
        LambdaQueryWrapper<Club> queryWrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            queryWrapper.eq(Club::getStatus, status);
        }
        queryWrapper.orderByDesc(Club::getCreatedAt);
        return list(queryWrapper);
    }

    @Override
    public Club createClub(Club club) {
        club.setStatus(0);
        save(club);
        return club;
    }

    @Override
    public Club updateClub(Club club) {
        updateById(club);
        return getById(club.getId());
    }

    @Override
    public void deleteClub(Long id) {
        removeById(id);
    }

    @Override
    public Club auditClub(Long id, Integer status) {
        Club club = getById(id);
        if (club != null) {
            club.setStatus(status);
            updateById(club);
        }
        return club;
    }
}