package com.example.unionsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.unionsystem.entity.ClubMember;
import com.example.unionsystem.mapper.ClubMemberMapper;
import com.example.unionsystem.service.ClubMemberService;
import com.example.unionsystem.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClubMemberServiceImpl extends ServiceImpl<ClubMemberMapper, ClubMember> implements ClubMemberService {

    @Override
    public List<Long> getManagedClubIds(Long userId) {
        LambdaQueryWrapper<ClubMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ClubMember::getUserId, userId)
                .eq(ClubMember::getRole, 1) // 1表示负责人
                .eq(ClubMember::getStatus, 1); // 状态为1表示已通过
        List<ClubMember> members = list(queryWrapper);
        return members.stream()
                .map(ClubMember::getClubId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isClubManager(Long userId, Long clubId) {
        LambdaQueryWrapper<ClubMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ClubMember::getUserId, userId)
                .eq(ClubMember::getClubId, clubId)
                .eq(ClubMember::getRole, 1)
                .eq(ClubMember::getStatus, 1);
        return count(queryWrapper) > 0;
    }

    @Override
    public List<ClubMember> listByClubId(Long clubId) {
        LambdaQueryWrapper<ClubMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ClubMember::getClubId, clubId);
        return list(queryWrapper);
    }

    @Override
    public List<ClubMember> listPendingMembers(Long clubId) {
        LambdaQueryWrapper<ClubMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ClubMember::getClubId, clubId)
                .eq(ClubMember::getStatus, 0); // 状态为0表示待审核
        return list(queryWrapper);
    }

    @Override
    public ClubMember approveMember(Long clubMemberId) {
        ClubMember member = getById(clubMemberId);
        if (member == null) {
            throw new RuntimeException("成员记录不存在");
        }
        member.setStatus(1);
        member.setJoinedAt(LocalDateTime.now());
        updateById(member);
        return member;
    }

    @Override
    public ClubMember rejectMember(Long clubMemberId) {
        ClubMember member = getById(clubMemberId);
        if (member == null) {
            throw new RuntimeException("成员记录不存在");
        }
        member.setStatus(2); // 状态为2表示拒绝
        updateById(member);
        return member;
    }

    @Override
    public ClubMember addMember(Long clubId, Long userId, Integer role) {
        // 检查是否已经存在
        LambdaQueryWrapper<ClubMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ClubMember::getClubId, clubId)
                .eq(ClubMember::getUserId, userId);
        ClubMember existing = getOne(queryWrapper);
        if (existing != null) {
            throw new RuntimeException("该用户已在社团中");
        }
        
        ClubMember member = new ClubMember();
        member.setClubId(clubId);
        member.setUserId(userId);
        member.setRole(role != null ? role : 0);
        member.setStatus(1); // 管理员直接添加的成员直接通过
        member.setJoinedAt(LocalDateTime.now());
        save(member);
        return member;
    }

    @Override
    public ClubMember updateMemberRole(Long clubMemberId, Integer role) {
        ClubMember member = getById(clubMemberId);
        if (member == null) {
            throw new RuntimeException("成员记录不存在");
        }
        if (role != 0 && role != 1 && role != 2) {
            throw new RuntimeException("无效的角色值");
        }
        member.setRole(role);
        updateById(member);
        return member;
    }

    @Override
    public void removeMember(Long clubMemberId) {
        removeById(clubMemberId);
    }

    @Override
    public ClubMember getMemberById(Long clubMemberId) {
        return getById(clubMemberId);
    }
}
