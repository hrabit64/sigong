package com.hrabit64.sigong.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.hrabit64.sigong.config.EmbeddedRedisConfig;
import com.hrabit64.sigong.config.TestRedisConfig;
import com.hrabit64.sigong.domain.user.User;
import com.hrabit64.sigong.domain.user.UserRepository;
import com.hrabit64.sigong.dto.file.serviceDto.FileInfoResponseServiceDto;
import com.hrabit64.sigong.dto.user.serviceDto.UserInfoResponseDto;
import com.hrabit64.sigong.service.FileInfoServiceImpl;
import com.hrabit64.sigong.service.UserServiceImpl;
import com.hrabit64.sigong.utils.FileUtils;
import com.hrabit64.sigong.utils.KeyGenerator;
import com.hrabit64.sigong.utils.user.UserUtils;
import com.hrabit64.sigong.utils.virusScan.VirusScanResult;
import com.hrabit64.sigong.utils.virusScan.VirusScanner;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import redis.embedded.RedisServer;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.hrabit64.sigong.config.SigongConfig.AllowFileTypes;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration(classes = {TestRedisConfig.class})
@SpringBootTest
class FileApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VirusScanner virusScanner;

    @MockBean
    private KeyGenerator keyGenerator;

    @MockBean
    private FileUtils fileUtils;

    @MockBean
    private FileInfoServiceImpl fileInfoService;

    @MockBean
    private UserServiceImpl userService;

    private final String testKey = "11111111111111111111111111111111";
    private final LocalDateTime testDateTime = LocalDateTime.parse("2022-08-04 04:16:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUpEach() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @AfterEach
    void tearDownEach() {
    }

    @Test
    void upload() throws Exception {

        String testFileName = "sigongjoah.pdf";
        File mockedFile = Mockito.mock(File.class);

        //ref https://stackoverflow.com/questions/21800726/using-spring-mvc-test-to-unit-test-multipart-post-request
        MockMultipartFile testFile = new MockMultipartFile("file", testFileName, "text/plain", "some xml".getBytes());

        VirusScanResult testVirusScanResult = VirusScanResult.builder()
                .virusScanResultLink("https://www.virustotal.com")
                .sha256("test")
                .sha1("test")
                .md5("test")
                .build();

        doReturn(testVirusScanResult).when(virusScanner).scanFile(any());
        doReturn(testKey).when(keyGenerator).generate();
        doReturn(new UserInfoResponseDto(User.builder().build())).when(userService).findByIP(anyString());
        doReturn(mockedFile).when(fileUtils).convert(testFile,testKey);

        mvc.perform(
                RestDocumentationRequestBuilders.fileUpload("/api/v1/file")
                .file(testFile))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.download_code").value(testKey))
                .andDo(print())
                .andDo(document("file-upload",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("download_code").description("해당 파일을 다운로드 및 조회할 수 있는 32자리의 랜덤 문자열 코드")
                        )
                ));

        verify(virusScanner).scanFile(any());
    }

    @Test
    void getFileInfo() throws Exception {
        FileInfoResponseServiceDto responseServiceDto = FileInfoResponseServiceDto.builder()
                .ip("0:0:0:0:0:0:0:1")
                .fileName(testKey+"_filename.pdf.sigong")
                .createdDateTime(testDateTime)
                .originFileName("filename.pdf")
                .md5("test md5")
                .sha1("test sha1")
                .sha256("test sha256")
                .virusScanResultLink("thanks for https://www.virustotal.com")
                .build();

        doReturn(responseServiceDto).when(fileInfoService).findByCode(testKey);
        doReturn(new UserInfoResponseDto(User.builder().build())).when(userService).findByIP(anyString());

        mvc.perform(
                RestDocumentationRequestBuilders.get("/api/v1/file/{downloadCode}",testKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.file_name").value(responseServiceDto.getOriginFileName()))
                .andExpect(jsonPath("$.md5").value(responseServiceDto.getMd5()))
                .andExpect(jsonPath("$.sha1").value(responseServiceDto.getSha1()))
                .andExpect(jsonPath("$.sha256").value(responseServiceDto.getSha256()))
                .andExpect(jsonPath("$.virus_scan_result_link").value(responseServiceDto.getVirusScanResultLink()))
                .andDo(print())
                .andDo(document("file-get-info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("downloadCode").description("조회할 코드")
                        ),
                        responseFields(
                                fieldWithPath("file_name").description("대상 attendance의 ID"),
                                fieldWithPath("md5").description("attendance가 생성된 일자"),
                                fieldWithPath("sha1").description("attendance의 마지막 수정 일자"),
                                fieldWithPath("sha256").description("attendance의 마지막 수정 일자"),
                                fieldWithPath("virus_scan_result_link").description("해당 attendance를 추가한 대상"),
                                fieldWithPath("created_date_time").description("해당 attendance를 마지막으로 수정한 대상")
                        )
                ));
    }

    @Test
    void deleteFile() throws Exception {

        FileInfoResponseServiceDto responseServiceDto = FileInfoResponseServiceDto.builder()
                .ip("0:0:0:0:0:0:0:1")
                .fileName(testKey+"_filename.pdf.sigong")
                .createdDateTime(testDateTime)
                .originFileName("filename.pdf")
                .md5("test md5")
                .sha1("test sha1")
                .sha256("test sha256")
                .virusScanResultLink("thanks for https://www.virustotal.com")
                .build();

        doReturn(responseServiceDto.toControllerDto()).when(fileInfoService).revoke(testKey);
        doReturn(new UserInfoResponseDto(User.builder().build())).when(userService).findByIP(anyString());

        mvc.perform(
                RestDocumentationRequestBuilders.delete("/api/v1/file/{downloadCode}",testKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.message").value("성공적으로 코드와 파일을 삭제했습니다."))
                .andDo(print())
                .andDo(document("file-revoke",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("downloadCode").description("삭제할 코드")
                        ),

                        responseFields(
                                fieldWithPath("message").description("성공 메시지")
                        )
                ));

    }

    @Test
    void download() throws Exception {
        String testFileName = "sigongjoah.pdf";
        File mockedFile = Mockito.mock(File.class);

        FileInfoResponseServiceDto responseServiceDto = FileInfoResponseServiceDto.builder()
                .ip("0:0:0:0:0:0:0:1")
                .fileName(testKey+"_"+testFileName+".sigong")
                .createdDateTime(testDateTime)
                .originFileName(testFileName)
                .md5("test md5")
                .sha1("test sha1")
                .sha256("test sha256")
                .virusScanResultLink("thanks for https://www.virustotal.com")
                .build();

        doReturn(responseServiceDto.toControllerDto()).when(fileInfoService).revoke(testKey);
        doReturn(responseServiceDto).when(fileInfoService).findByCode(testKey);
        doReturn(new UserInfoResponseDto(User.builder().build())).when(userService).findByIP(anyString());
        doReturn(true).when(mockedFile).exists();
        doReturn(mockedFile).when(fileUtils).find(responseServiceDto.getFileName());
        doReturn(mockedFile).when(fileUtils).convertOrigin(mockedFile,responseServiceDto.getOriginFileName());
        doReturn(testFileName).when(mockedFile).getName();

        mvc.perform(
                RestDocumentationRequestBuilders.get("/api/v1/file/{downloadCode}/download",testKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(header().string("Content-Disposition","attachment;filename=\""+testFileName+"\""))
                .andDo(document("file-download",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("downloadCode").description("다운로드할 코드")
                        )
                ));

    }
}