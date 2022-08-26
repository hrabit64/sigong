package com.hrabit64.sigong.service;

import com.hrabit64.sigong.domain.user.User;
import com.hrabit64.sigong.domain.user.UserRepository;
import com.hrabit64.sigong.dto.user.serviceDto.UserInfoResponseDto;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hrabit64.sigong.config.SigongConfig.BanTime;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @Transactional
    public void addFileCnt(String ip,Integer amount) {

        User target = userRepository.findById(ip)
                .orElse(User.builder().build());

        if(target.getIp() == null){

            if(amount < -1) return;

            userRepository.save(User.builder()
                    .ip(ip)
                    .fileCnt(amount)
                    .isBan(false)
                    .ttl(10L)
                    .build());

            return;
        }
        if ((target.getFileCnt() + amount >= 0)) {
            target.addFileCnt(amount);
        } else {
            target.setFileCnt(0);
        }

        target.setTtl(10L);
        userRepository.save(target);
    }

    @Override
    @Transactional
    public void addBan(String ip) {

        User target = userRepository.findById(ip)
                .orElse(User.builder().build());

        if(target.getIp() == null){
            userRepository.save(User.builder()
                    .ip(ip)
                    .fileCnt(0)
                    .isBan(true)
                    .ttl(BanTime)
                    .build());
            return;
        }
        target.setIsBan(true);
        userRepository.save(target);
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfoResponseDto findByIP(String ip) {

        return new UserInfoResponseDto(userRepository.findById(ip)
                .orElse(User.builder().build()));

    }
}
