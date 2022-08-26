package com.hrabit64.sigong.service;

import com.hrabit64.sigong.dto.user.serviceDto.UserInfoResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    void addFileCnt(String ip,Integer amount);
    void addBan(String ip);
    UserInfoResponseDto findByIP(String ip);
}
