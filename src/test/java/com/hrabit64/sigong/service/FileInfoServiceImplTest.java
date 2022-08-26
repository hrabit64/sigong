package com.hrabit64.sigong.service;

import com.hrabit64.sigong.domain.file.FileInfo;
import com.hrabit64.sigong.domain.file.FileInfoRepository;
import com.hrabit64.sigong.dto.file.controllerDto.FileInfoResponseControllerDto;
import com.hrabit64.sigong.dto.file.serviceDto.FileInfoAddRequestServiceDto;
import com.hrabit64.sigong.dto.file.serviceDto.FileInfoResponseServiceDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static com.hrabit64.sigong.config.SigongConfig.CodeExpireTime;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ComponentScan
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class FileInfoServiceImplTest {

    @InjectMocks
    private FileInfoServiceImpl fileInfoService;

    @Mock
    private FileInfoRepository fileInfoRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void add() {
        //given
        FileInfoAddRequestServiceDto addRequestServiceDto = FileInfoAddRequestServiceDto.builder()
                .ip("0:0:0:0:0:0:0:1")
                .virusScanResultLink("link")
                .md5("123")
                .sha1("123")
                .sha256("123")
                .fileName("test.sigong")
                .originFileName("test.pdf")
                .code("1234")
                .build();

        FileInfoAddRequestServiceDto testDto = Mockito.mock(FileInfoAddRequestServiceDto.class);

        FileInfo target = addRequestServiceDto.toEntity(CodeExpireTime);
        doReturn(target).when(testDto).toEntity(CodeExpireTime);

        //when
        fileInfoService.add(testDto);

        //then
        verify(fileInfoRepository).save(target);
    }

    @Test
    void revoke() {

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

        String targetCode = testFileInfo.getCode();

        doReturn(Optional.of(testFileInfo)).when(fileInfoRepository).findById(targetCode);

        //when
        FileInfoResponseControllerDto result = fileInfoService.revoke(targetCode);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result,new FileInfoResponseControllerDto(testFileInfo));
        verify(fileInfoRepository).findById(targetCode);
        verify(fileInfoRepository).delete(testFileInfo);
    }

    @Test
    void findByCode() {
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

        String targetCode = testFileInfo.getCode();

        doReturn(Optional.of(testFileInfo)).when(fileInfoRepository).findById(targetCode);

        //when
        FileInfoResponseServiceDto result = fileInfoService.findByCode(targetCode);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result,new FileInfoResponseServiceDto(testFileInfo));
        verify(fileInfoRepository).findById(targetCode);
    }

    @Test
    void findByFileName() {
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

        String targetFileName = testFileInfo.getFileName();

        doReturn(Optional.of(testFileInfo)).when(fileInfoRepository).findFileInfoByFileName(targetFileName);

        //when
        FileInfoResponseServiceDto result = fileInfoService.findByFileName(targetFileName);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result,new FileInfoResponseServiceDto(testFileInfo));
        verify(fileInfoRepository).findFileInfoByFileName(targetFileName);
    }
}