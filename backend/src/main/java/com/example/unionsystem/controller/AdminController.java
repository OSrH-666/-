package com.example.unionsystem.controller;

import com.example.unionsystem.dto.response.ApiResponse;
import com.example.unionsystem.entity.Activity;
import com.example.unionsystem.entity.Club;
import com.example.unionsystem.entity.ClubMember;
import com.example.unionsystem.entity.User;
import com.example.unionsystem.service.ActivityService;
import com.example.unionsystem.service.ClubMemberService;
import com.example.unionsystem.service.ClubService;
import com.example.unionsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClubService clubService;

    @Autowired
    private ClubMemberService clubMemberService;

    @Autowired
    private ActivityService activityService;

    // ==================== 用户管理 ====================

    @GetMapping("/users")
    public ApiResponse<List<User>> listAllUsers() {
        List<User> users = userService.listAllUsers();
        return ApiResponse.success(users);
    }

    @GetMapping("/users/{userId}")
    public ApiResponse<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ApiResponse.success(user);
    }

    @PutMapping("/users/{userId}/role")
    public ApiResponse<User> updateUserRole(@PathVariable Long userId, @RequestParam String role) {
        User user = userService.updateUserRole(userId, role);
        return ApiResponse.success("角色更新成功", user);
    }

    @PutMapping("/users/{userId}/status")
    public ApiResponse<User> updateUserStatus(@PathVariable Long userId, @RequestParam Integer status) {
        User user = userService.updateUserStatus(userId, status);
        String message = status == 1 ? "用户已启用" : "用户已禁用";
        return ApiResponse.success(message, user);
    }

    // ==================== 社团管理 ====================

    @GetMapping("/clubs")
    public ApiResponse<List<Club>> listAllClubs(@RequestParam(required = false) Integer status) {
        List<Club> clubs = clubService.listClubs(status);
        return ApiResponse.success(clubs);
    }

    @GetMapping("/clubs/pending")
    public ApiResponse<List<Club>> listPendingClubs() {
        List<Club> clubs = clubService.listClubs(0);
        return ApiResponse.success(clubs);
    }

    @PutMapping("/clubs/{clubId}/audit")
    public ApiResponse<Club> auditClub(@PathVariable Long clubId, @RequestParam Integer status) {
        Club club = clubService.auditClub(clubId, status);
        String message;
        if (status == 1) {
            message = "社团审核通过";
        } else if (status == 2) {
            message = "社团审核拒绝";
        } else {
            message = "审核状态更新成功";
        }
        return ApiResponse.success(message, club);
    }

    @DeleteMapping("/clubs/{clubId}")
    public ApiResponse<Void> deleteClub(@PathVariable Long clubId) {
        clubService.deleteClub(clubId);
        return ApiResponse.success("社团删除成功", null);
    }

    // ==================== 社团成员管理 ====================

    @GetMapping("/clubs/{clubId}/members")
    public ApiResponse<List<ClubMember>> listClubMembers(@PathVariable Long clubId) {
        List<ClubMember> members = clubMemberService.listByClubId(clubId);
        return ApiResponse.success(members);
    }

    @GetMapping("/clubs/{clubId}/members/pending")
    public ApiResponse<List<ClubMember>> listPendingMembers(@PathVariable Long clubId) {
        List<ClubMember> members = clubMemberService.listPendingMembers(clubId);
        return ApiResponse.success(members);
    }

    @PostMapping("/clubs/{clubId}/members")
    public ApiResponse<ClubMember> addClubMember(@PathVariable Long clubId, 
                                                 @RequestParam Long userId, 
                                                 @RequestParam(required = false) Integer role) {
        ClubMember member = clubMemberService.addMember(clubId, userId, role);
        return ApiResponse.success("成员添加成功", member);
    }

    @PutMapping("/club-members/{memberId}/approve")
    public ApiResponse<ClubMember> approveClubMember(@PathVariable Long memberId) {
        ClubMember member = clubMemberService.approveMember(memberId);
        return ApiResponse.success("成员审核通过", member);
    }

    @PutMapping("/club-members/{memberId}/reject")
    public ApiResponse<ClubMember> rejectClubMember(@PathVariable Long memberId) {
        ClubMember member = clubMemberService.rejectMember(memberId);
        return ApiResponse.success("成员审核拒绝", member);
    }

    @PutMapping("/club-members/{memberId}/role")
    public ApiResponse<ClubMember> updateMemberRole(@PathVariable Long memberId, @RequestParam Integer role) {
        ClubMember member = clubMemberService.updateMemberRole(memberId, role);
        return ApiResponse.success("成员角色更新成功", member);
    }

    @DeleteMapping("/club-members/{memberId}")
    public ApiResponse<Void> removeClubMember(@PathVariable Long memberId) {
        clubMemberService.removeMember(memberId);
        return ApiResponse.success("成员移除成功", null);
    }

    // ==================== 统计数据 ====================

    @GetMapping("/statistics")
    public ApiResponse<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 用户统计
        long totalUsers = userService.count();
        stats.put("totalUsers", totalUsers);
        
        // 社团统计
        long totalClubs = clubService.count();
        List<Club> pendingClubs = clubService.listClubs(0);
        stats.put("totalClubs", totalClubs);
        stats.put("pendingClubs", pendingClubs.size());
        
        // 活动统计
        long totalActivities = activityService.count();
        stats.put("totalActivities", totalActivities);
        
        return ApiResponse.success(stats);
    }
}
