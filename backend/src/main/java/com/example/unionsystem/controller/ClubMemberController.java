package com.example.unionsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.unionsystem.dto.response.ApiResponse;
import com.example.unionsystem.entity.ClubMember;
import com.example.unionsystem.entity.User;
import com.example.unionsystem.service.ClubMemberService;
import com.example.unionsystem.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/club-member")
public class ClubMemberController {

    @Autowired
    private ClubMemberService clubMemberService;

    // 学生申请加入社团
    @PostMapping("/apply")
    public ApiResponse<ClubMember> applyToClub(@RequestParam Long clubId) {
        User currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("请先登录");
        }

        // 检查是否已经申请过
        LambdaQueryWrapper<ClubMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ClubMember::getClubId, clubId)
                .eq(ClubMember::getUserId, currentUser.getId());
        ClubMember existing = clubMemberService.getOne(queryWrapper);
        
        if (existing != null) {
            if (existing.getStatus() == 0) {
                throw new RuntimeException("您已提交申请，正在审核中");
            } else if (existing.getStatus() == 1) {
                throw new RuntimeException("您已经是该社团成员");
            }
        }

        ClubMember member = new ClubMember();
        member.setClubId(clubId);
        member.setUserId(currentUser.getId());
        member.setRole(0); // 普通成员
        member.setStatus(0); // 待审核
        clubMemberService.save(member);
        return ApiResponse.success("申请提交成功，请等待审核", member);
    }

    // 学生查看自己的社团成员记录
    @GetMapping("/my")
    public ApiResponse<List<ClubMember>> getMyMemberships() {
        User currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("请先登录");
        }

        LambdaQueryWrapper<ClubMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ClubMember::getUserId, currentUser.getId());
        List<ClubMember> members = clubMemberService.list(queryWrapper);
        return ApiResponse.success(members);
    }

    // 社团负责人查看自己管理社团的成员（待审核）
    @GetMapping("/club/{clubId}/pending")
    public ApiResponse<List<ClubMember>> getPendingMembers(@PathVariable Long clubId) {
        User currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("请先登录");
        }

        // 验证是否是该社团负责人
        if (!SecurityUtil.isAdmin() && !clubMemberService.isClubManager(currentUser.getId(), clubId)) {
            throw new RuntimeException("您无权查看该社团的成员");
        }

        List<ClubMember> members = clubMemberService.listPendingMembers(clubId);
        return ApiResponse.success(members);
    }

    // 社团负责人查看自己管理社团的所有成员
    @GetMapping("/club/{clubId}/all")
    public ApiResponse<List<ClubMember>> getAllMembers(@PathVariable Long clubId) {
        User currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("请先登录");
        }

        // 验证是否是该社团负责人或管理员
        if (!SecurityUtil.isAdmin() && !clubMemberService.isClubManager(currentUser.getId(), clubId)) {
            throw new RuntimeException("您无权查看该社团的成员");
        }

        List<ClubMember> members = clubMemberService.listByClubId(clubId);
        return ApiResponse.success(members);
    }

    // 社团负责人审核通过成员申请
    @PutMapping("/{memberId}/approve")
    public ApiResponse<ClubMember> approveMember(@PathVariable Long memberId) {
        User currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("请先登录");
        }

        ClubMember member = clubMemberService.getMemberById(memberId);
        if (member == null) {
            throw new RuntimeException("成员记录不存在");
        }

        // 验证权限
        if (!SecurityUtil.isAdmin() && !clubMemberService.isClubManager(currentUser.getId(), member.getClubId())) {
            throw new RuntimeException("您无权审核该成员");
        }

        ClubMember approved = clubMemberService.approveMember(memberId);
        return ApiResponse.success("成员审核通过", approved);
    }

    // 社团负责人审核拒绝成员申请
    @PutMapping("/{memberId}/reject")
    public ApiResponse<ClubMember> rejectMember(@PathVariable Long memberId) {
        User currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("请先登录");
        }

        ClubMember member = clubMemberService.getMemberById(memberId);
        if (member == null) {
            throw new RuntimeException("成员记录不存在");
        }

        // 验证权限
        if (!SecurityUtil.isAdmin() && !clubMemberService.isClubManager(currentUser.getId(), member.getClubId())) {
            throw new RuntimeException("您无权审核该成员");
        }

        ClubMember rejected = clubMemberService.rejectMember(memberId);
        return ApiResponse.success("成员审核拒绝", rejected);
    }

    // 社团负责人更新成员角色
    @PutMapping("/{memberId}/role")
    public ApiResponse<ClubMember> updateMemberRole(@PathVariable Long memberId, @RequestParam Integer role) {
        User currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("请先登录");
        }

        ClubMember member = clubMemberService.getMemberById(memberId);
        if (member == null) {
            throw new RuntimeException("成员记录不存在");
        }

        // 验证权限
        if (!SecurityUtil.isAdmin() && !clubMemberService.isClubManager(currentUser.getId(), member.getClubId())) {
            throw new RuntimeException("您无权修改该成员的角色");
        }

        ClubMember updated = clubMemberService.updateMemberRole(memberId, role);
        return ApiResponse.success("成员角色更新成功", updated);
    }

    // 社团负责人移除成员
    @DeleteMapping("/{memberId}")
    public ApiResponse<Void> removeMember(@PathVariable Long memberId) {
        User currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("请先登录");
        }

        ClubMember member = clubMemberService.getMemberById(memberId);
        if (member == null) {
            throw new RuntimeException("成员记录不存在");
        }

        // 验证权限
        if (!SecurityUtil.isAdmin() && !clubMemberService.isClubManager(currentUser.getId(), member.getClubId())) {
            throw new RuntimeException("您无权移除该成员");
        }

        clubMemberService.removeMember(memberId);
        return ApiResponse.success("成员移除成功", null);
    }
}
