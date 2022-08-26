package com.hrabit64.sigong.service;

import com.hrabit64.sigong.domain.file.FileInfoRepository;
import com.hrabit64.sigong.domain.user.User;
import com.hrabit64.sigong.domain.user.UserRepository;
import com.hrabit64.sigong.dto.file.controllerDto.FileInfoResponseControllerDto;
import com.hrabit64.sigong.dto.user.serviceDto.UserInfoResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.hrabit64.sigong.config.SigongConfig.BanTime;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ComponentScan
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addFileCntNewUser() {
        //given
        User testUserInfo = User.builder()
                .ip("0:0:0:0:0:0:0:1")
                .fileCnt(1)
                .isBan(false)
                .ttl(10L)
                .build();

        doReturn(Optional.of(User.builder().build())).when(userRepository).findById(testUserInfo.getIp());

        //when
        userService.addFileCnt(testUserInfo.getIp(),1);

        //then
        verify(userRepository).save(testUserInfo);
        verify(userRepository).findById(testUserInfo.getIp());

    }

    @Test
    void addFileCnt() {
        //given
        User testUserInfo = User.builder()
                .ip("0:0:0:0:0:0:0:1")
                .fileCnt(1)
                .isBan(false)
                .ttl(10L)
                .build();

        doReturn(Optional.of(testUserInfo)).when(userRepository).findById(testUserInfo.getIp());

        //when
        userService.addFileCnt(testUserInfo.getIp(),1);

        //then
        testUserInfo.setFileCnt(2);
        verify(userRepository).save(testUserInfo);
        verify(userRepository).findById(testUserInfo.getIp());

    }

    @Test
    void addBanNewUser() {
        //given
        User testUserInfo = User.builder()
                .ip("0:0:0:0:0:0:0:1")
                .fileCnt(0)
                .isBan(false)
                .ttl(BanTime)
                .build();

        doReturn(Optional.of(User.builder().build())).when(userRepository).findById(testUserInfo.getIp());

        //when
        userService.addBan(testUserInfo.getIp());

        //then
        testUserInfo.setIsBan(true);
        verify(userRepository).findById(testUserInfo.getIp());
        verify(userRepository).save(testUserInfo);
    }

    @Test
    void addBan() {
        //given
        User testUserInfo = User.builder()
                .ip("0:0:0:0:0:0:0:1")
                .fileCnt(0)
                .isBan(false)
                .ttl(10L)
                .build();

        doReturn(Optional.of(testUserInfo)).when(userRepository).findById(testUserInfo.getIp());

        //when
        userService.addBan(testUserInfo.getIp());

        //then
        testUserInfo.setIsBan(true);
        testUserInfo.setTtl(BanTime);
        verify(userRepository).findById(testUserInfo.getIp());
        verify(userRepository).save(testUserInfo);
    }

    @Test
    void findByIP() {
        //given
        User testUserInfo = User.builder()
                .ip("0:0:0:0:0:0:0:1")
                .fileCnt(0)
                .isBan(false)
                .ttl(10L)
                .build();

        doReturn(Optional.of(testUserInfo)).when(userRepository).findById(testUserInfo.getIp());

        //when
        UserInfoResponseDto result =  userService.findByIP(testUserInfo.getIp());

        //then
        Assertions.assertEquals(result,new UserInfoResponseDto(testUserInfo));
        verify(userRepository).findById(testUserInfo.getIp());

    }
}