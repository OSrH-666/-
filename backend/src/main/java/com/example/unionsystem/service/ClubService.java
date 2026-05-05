package com.example.unionsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.unionsystem.entity.Club;

import java.util.List;

public interface ClubService extends IService<Club> {

    List<Club> listClubs(Integer status);

    Club createClub(Club club);

    Club updateClub(Club club);

    void deleteClub(Long id);

    Club auditClub(Long id, Integer status);
}