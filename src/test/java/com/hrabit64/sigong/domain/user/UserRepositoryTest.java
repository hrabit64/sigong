package com.hrabit64.sigong.domain.user;

import com.hrabit64.sigong.config.EmbeddedRedisConfig;
import com.hrabit64.sigong.config.TestRedisConfig;
import com.hrabit64.sigong.domain.file.FileInfo;
import com.hrabit64.sigong.domain.file.FileInfoRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {EmbeddedRedisConfig.class, TestRedisConfig.class})
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DataRedisTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @AfterEach
    void tearDownEach(){
        userRepository.deleteAll();
    }


    @DisplayName("user 등록")
    @Test
    void save(){

        //given
        User testUserInfo = User.builder()
                .ip("0:0:0:0:0:0:0:1")
                .fileCnt(0)
                .isBan(false)
                .ttl(10L)
                .build();

        //when
        userRepository.save(testUserInfo);

        //then
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        assertEquals(testUserInfo.getIp(), Objects.requireNonNull(hashOperations.get("user:" + testUserInfo.getIp(), "ip")).toString());
        assertEquals(testUserInfo.getFileCnt().toString(), Objects.requireNonNull(hashOperations.get("user:" + testUserInfo.getIp(), "fileCnt")).toString());
        assertEquals(testUserInfo.getIsBan() ? "1" : "0", Objects.requireNonNull(hashOperations.get("user:" + testUserInfo.getIp(), "isBan")).toString());
    }

    @DisplayName("user 삭제")
    @Test
    void delete(){

        //given
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();

        User testUserInfo = User.builder()
                .ip("0:0:0:0:0:0:0:1")
                .fileCnt(0)
                .isBan(false)
                .ttl(10L)
                .build();

        hashOperations.put("user:"+testUserInfo.getIp(),"ip",testUserInfo.getIp());
        hashOperations.put("user:"+testUserInfo.getIp(),"fileCnt",testUserInfo.getFileCnt().toString());
        hashOperations.put("user:"+testUserInfo.getIp(),"isBan",testUserInfo.getIsBan() ? "1" : "0");
        hashOperations.put("user:"+testUserInfo.getIp(),"ttl",testUserInfo.getTtl().toString());

        //when
        userRepository.delete(testUserInfo);

        //then
        assertThrows(NullPointerException.class,() -> hashOperations.get("user:" + testUserInfo.getIp(), "ip").toString());
    }

    @DisplayName("user IP로 조회")
    @Test
    void findById(){

        //given
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();

        User testUserInfo = User.builder()
                .ip("0:0:0:0:0:0:0:1")
                .fileCnt(0)
                .isBan(false)
                .ttl(10L)
                .build();

        hashOperations.put("user:"+testUserInfo.getIp(),"ip",testUserInfo.getIp());
        hashOperations.put("user:"+testUserInfo.getIp(),"fileCnt",testUserInfo.getFileCnt().toString());
        hashOperations.put("user:"+testUserInfo.getIp(),"isBan",testUserInfo.getIsBan() ? "1" : "0");
        hashOperations.put("user:"+testUserInfo.getIp(),"ttl",testUserInfo.getTtl().toString());


        //when
        User result =  userRepository.findById(testUserInfo.getIp()).orElseThrow();

        //then
        assertThat(result.equalsExcludeTTL(testUserInfo),is(true));
    }

}