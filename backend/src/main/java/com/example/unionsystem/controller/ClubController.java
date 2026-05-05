package com.example.unionsystem.controller;

import com.example.unionsystem.dto.response.ApiResponse;
import com.example.unionsystem.entity.Club;
import com.example.unionsystem.service.ClubService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/club")
public class ClubController {

    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @GetMapping
    public ApiResponse<List<Club>> list(@RequestParam(required = false) Integer status) {
        List<Club> clubs = clubService.listClubs(status);
        return ApiResponse.success(clubs);
    }

    @GetMapping("/{id}")
    public ApiResponse<Club> getById(@PathVariable Long id) {
        Club club = clubService.getById(id);
        return ApiResponse.success(club);
    }

    @PostMapping
    public ApiResponse<Club> create(@RequestBody Club club) {
        Club saved = clubService.createClub(club);
        return ApiResponse.success("创建成功", saved);
    }

    @PutMapping("/{id}")
    public ApiResponse<Club> update(@PathVariable Long id, @RequestBody Club club) {
        club.setId(id);
        Club updated = clubService.updateClub(club);
        return ApiResponse.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        clubService.deleteClub(id);
        return ApiResponse.success("删除成功", null);
    }

    @PutMapping("/{id}/audit")
    public ApiResponse<Club> audit(@PathVariable Long id, @RequestParam Integer status) {
        Club updated = clubService.auditClub(id, status);
        return ApiResponse.success("审核完成", updated);
    }
}