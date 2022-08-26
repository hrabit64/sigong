package com.hrabit64.sigong.domain.file;

import com.hrabit64.sigong.config.EmbeddedRedisConfig;
import com.hrabit64.sigong.config.TestRedisConfig;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
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
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {EmbeddedRedisConfig.class, TestRedisConfig.class})
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DataRedisTest
class FileInfoRepositoryTest {

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @AfterEach
    void tearDownEach(){
        fileInfoRepository.deleteAll();
    }

    @DisplayName("file info 등록")
    @Test
    void save(){

        //given
        FileInfo testFileInfo = FileInfo.builder()
                .ip("0:0:0:0:0:0:0:1")
                .ttl(10L)
                .virusScanResultLink("link")
                .md5("123")
                .sha1("123")
                .sha256("123")
                .fileName("test.sigong")
                .originFileName("test.pdf")
                .code("1234")
                .createdDateTime(LocalDateTime.now())
                .build();
        //when
        fileInfoRepository.save(testFileInfo);

        //then
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        assertEquals(testFileInfo.getIp(), Objects.requireNonNull(hashOperations.get("fileinfo:" + testFileInfo.getCode(), "ip")).toString());
        assertEquals(testFileInfo.getTtl().toString(), Objects.requireNonNull(hashOperations.get("fileinfo:" + testFileInfo.getCode(), "ttl")).toString());
        assertEquals(testFileInfo.getVirusScanResultLink(), Objects.requireNonNull(hashOperations.get("fileinfo:" + testFileInfo.getCode(), "virusScanResultLink")).toString());
        assertEquals(testFileInfo.getMd5(), Objects.requireNonNull(hashOperations.get("fileinfo:" + testFileInfo.getCode(), "md5")).toString());
        assertEquals(testFileInfo.getSha1(), Objects.requireNonNull(hashOperations.get("fileinfo:" + testFileInfo.getCode(), "sha1")).toString());
        assertEquals(testFileInfo.getSha256(), Objects.requireNonNull(hashOperations.get("fileinfo:" + testFileInfo.getCode(), "sha256")).toString());
        assertEquals(testFileInfo.getFileName(), Objects.requireNonNull(hashOperations.get("fileinfo:" + testFileInfo.getCode(), "fileName")).toString());
        assertEquals(testFileInfo.getOriginFileName(), Objects.requireNonNull(hashOperations.get("fileinfo:" + testFileInfo.getCode(), "originFileName")).toString());
        assertEquals(testFileInfo.getCreatedDateTime().toString(), Objects.requireNonNull(hashOperations.get("fileinfo:" + testFileInfo.getCode(), "createdDateTime")).toString());
        assertEquals(testFileInfo.getCode(), Objects.requireNonNull(hashOperations.get("fileinfo:" + testFileInfo.getCode(), "code")).toString());

    }

    @DisplayName("file info 삭제")
    @Test
    void delete(){
        //given
        FileInfo testFileInfo = FileInfo.builder()
                .ip("0:0:0:0:0:0:0:1")
                .ttl(10L)
                .virusScanResultLink("link")
                .md5("123")
                .sha1("123")
                .sha256("123")
                .fileName("test.sigong")
                .originFileName("test.pdf")
                .code("1234")
                .createdDateTime(LocalDateTime.now())
                .build();

        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put("fileinfo:" + testFileInfo.getCode(),"ip",testFileInfo.getIp());
        hashOperations.put("fileinfo:" + testFileInfo.getCode(),"ttl",testFileInfo.getTtl().toString());
        hashOperations.put("fileinfo:" + testFileInfo.getCode(),"virusScanResultLink",testFileInfo.getVirusScanResultLink());
        hashOperations.put("fileinfo:" + testFileInfo.getCode(),"md5",testFileInfo.getMd5());
        hashOperations.put("fileinfo:" + testFileInfo.getCode(),"sha1",testFileInfo.getSha1());
        hashOperations.put("fileinfo:" + testFileInfo.getCode(),"sha256",testFileInfo.getSha256());
        hashOperations.put("fileinfo:" + testFileInfo.getCode(),"fileName",testFileInfo.getFileName());
        hashOperations.put("fileinfo:" + testFileInfo.getCode(),"originFileName",testFileInfo.getOriginFileName());
        hashOperations.put("fileinfo:" + testFileInfo.getCode(),"code",testFileInfo.getCode());
        hashOperations.put("fileinfo:" + testFileInfo.getCode(),"createdDateTime",testFileInfo.getCreatedDateTime().toString());

        //when
        fileInfoRepository.delete(testFileInfo);

        //then
        assertThrows(NullPointerException.class,() -> hashOperations.get("fileinfo:" + testFileInfo.getCode(), "ip").toString());
    }

    @DisplayName("file info Code로 조회")
    @Test
    void findById(){
        //given
        FileInfo testFileInfo = FileInfo.builder()
                .ip("0:0:0:0:0:0:0:1")
                .ttl(10L)
                .virusScanResultLink("link")
                .md5("123")
                .sha1("123")
                .sha256("123")
                .fileName("test.sigong")
                .originFileName("test.pdf")
                .code("1234")
                .createdDateTime(LocalDateTime.now())
                .build();

        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put("fileinfo:" + testFileInfo.getCode(),"ip",testFileInfo.getIp());
        hashOperations.put("fileinfo:" + testFileInfo.getCode(),"ttl",testFileInfo.getTtl().toString());
        hashOperations.put("fileinfo:" + testFileInfo.getCode(),"virusScanResultLink",testFileInfo.getVirusScanResultLink());
        hashOperations.put("fileinfo:" + testFileInfo.getCode(),"md5",testFileInfo.getMd5());
        hashOperations.put("fileinfo:" + testFileInfo.getCode(),"sha1",testFileInfo.getSha1());
        hashOperations.put("fileinfo:" + testFileInfo.getCode(),"sha256",testFileInfo.getSha256());
        hashOperations.put("fileinfo:" + testFileInfo.getCode(),"fileName",testFileInfo.getFileName());
        hashOperations.put("fileinfo:" + testFileInfo.getCode(),"originFileName",testFileInfo.getOriginFileName());
        hashOperations.put("fileinfo:" + testFileInfo.getCode(),"code",testFileInfo.getCode());
        hashOperations.put("fileinfo:" + testFileInfo.getCode(),"createdDateTime",testFileInfo.getCreatedDateTime().toString());

        //when
        FileInfo result = fileInfoRepository.findById(testFileInfo.getCode()).orElseThrow();

        //then
        assertThat(result.equalsExcludeTTL(testFileInfo),is(true));
    }

    @DisplayName("file info fileName으로 조회")
    @Test
    void findFileInfoByFileName() {
        //given
        FileInfo testFileInfo = FileInfo.builder()
                .ip("0:0:0:0:0:0:0:1")
                .ttl(10L)
                .virusScanResultLink("link")
                .md5("123")
                .sha1("123")
                .sha256("123")
                .fileName("test.sigong")
                .originFileName("test.pdf")
                .code("1234")
                .createdDateTime(LocalDateTime.now())
                .build();

        fileInfoRepository.save(testFileInfo);

        //when
        FileInfo result = fileInfoRepository.findFileInfoByFileName(testFileInfo.getFileName()).orElseThrow();

        //then
        assertThat(result.equalsExcludeTTL(testFileInfo),is(true));
    }
}