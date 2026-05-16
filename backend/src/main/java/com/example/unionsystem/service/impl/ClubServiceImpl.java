package com.example.unionsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.unionsystem.entity.Club;
import com.example.unionsystem.mapper.ClubMapper;
import com.example.unionsystem.service.ClubMemberService;
import com.example.unionsystem.service.ClubService;
import com.example.unionsystem.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubServiceImpl extends ServiceImpl<ClubMapper, Club> implements ClubService {

    @Autowired
    private ClubMemberService clubMemberService;

    @Override
    public List<Club> listClubs(Integer status) {
        LambdaQueryWrapper<Club> queryWrapper = new LambdaQueryWrapper<>();
        
        // 如果是社团负责人，只能查看自己管理的社团
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (SecurityUtil.isClubManager() && currentUserId != null) {
            List<Long> managedClubIds = clubMemberService.getManagedClubIds(currentUserId);
            if (managedClubIds.isEmpty()) {
                return List.of();
            }
            queryWrapper.in(Club::getId, managedClubIds);
        }
        
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
        Long currentUserId = SecurityUtil.getCurrentUserId();
        Club existingClub = getById(club.getId());
        if (existingClub == null) {
            throw new RuntimeException("社团不存在");
        }
        
        if (SecurityUtil.isClubManager() && currentUserId != null) {
            if (!clubMemberService.isClubManager(currentUserId, club.getId())) {
                throw new RuntimeException("您不是该社团的负责人，无权修改社团信息");
            }
        }
        updateById(club);
        return getById(club.getId());
    }

    @Override
    public void deleteClub(Long id) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        Club existingClub = getById(id);
        if (existingClub == null) {
            throw new RuntimeException("社团不存在");
        }
        
        if (SecurityUtil.isClubManager() && currentUserId != null) {
            if (!clubMemberService.isClubManager(currentUserId, id)) {
                throw new RuntimeException("您不是该社团的负责人，无权删除社团");
            }
        }
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