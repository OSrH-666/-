package com.example.unionsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.unionsystem.entity.ClubMember;

import java.util.List;

public interface ClubMemberService extends IService<ClubMember> {
    
    List<Long> getManagedClubIds(Long userId);
    
    boolean isClubManager(Long userId, Long clubId);

    List<ClubMember> listByClubId(Long clubId);

    List<ClubMember> listPendingMembers(Long clubId);

    ClubMember approveMember(Long clubMemberId);

    ClubMember rejectMember(Long clubMemberId);

    ClubMember addMember(Long clubId, Long userId, Integer role);

    ClubMember updateMemberRole(Long clubMemberId, Integer role);

    void removeMember(Long clubMemberId);

    ClubMember getMemberById(Long clubMemberId);
}
