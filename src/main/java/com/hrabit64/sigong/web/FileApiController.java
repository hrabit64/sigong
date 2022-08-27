package com.hrabit64.sigong.web;

import com.hrabit64.sigong.dto.file.controllerDto.DownloadCodeResponseControllerDto;
import com.hrabit64.sigong.dto.file.controllerDto.FileInfoResponseControllerDto;
import com.hrabit64.sigong.dto.file.serviceDto.FileInfoAddRequestServiceDto;
import com.hrabit64.sigong.dto.file.serviceDto.FileInfoResponseServiceDto;
import com.hrabit64.sigong.dto.general.controllerDto.MessageDto;
import com.hrabit64.sigong.exception.ErrorCode;
import com.hrabit64.sigong.exception.SigongException;
import com.hrabit64.sigong.service.FileInfoServiceImpl;
import com.hrabit64.sigong.service.UserServiceImpl;
import com.hrabit64.sigong.utils.FileUtils;
import com.hrabit64.sigong.utils.KeyGenerator;
import com.hrabit64.sigong.utils.user.UserUtils;
import com.hrabit64.sigong.utils.validation.FileTypeCheck;
import com.hrabit64.sigong.utils.validation.UserCheck;
import com.hrabit64.sigong.utils.virusScan.VirusScanResult;
import com.hrabit64.sigong.utils.virusScan.VirusScanner;
import com.kanishka.virustotal.exception.QuotaExceededException;
import com.kanishka.virustotal.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/file")
public class FileApiController {

    private final FileUtils fileUtils;
    private final VirusScanner virusScanner;
    private final KeyGenerator keyGenerator;
    private final UserUtils userUtils;

    private final FileInfoServiceImpl fileInfoService;
    private final UserServiceImpl userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping
    public ResponseEntity<DownloadCodeResponseControllerDto> upload
            (@RequestParam("file") @NotNull @FileTypeCheck MultipartFile file,
             @UserCheck(isLimit = true) HttpServletRequest request)
            throws IOException, UnauthorizedAccessException, QuotaExceededException {

        String code = keyGenerator.generate();
        File convertedFile = fileUtils.convert(file,code);

        VirusScanResult virusScanResult = virusScanner.scanFile(convertedFile);

        fileInfoService.add(FileInfoAddRequestServiceDto.builder()
                        .code(code)
                        .fileName(convertedFile.getName())
                        .originFileName(file.getOriginalFilename())
                        .ip(request.getRemoteAddr())
                        .md5(virusScanResult.getMd5())
                        .sha1(virusScanResult.getSha1())
                        .sha256(virusScanResult.getSha256())
                        .virusScanResultLink(virusScanResult.getVirusScanResultLink())
                        .build());

        userService.addFileCnt(request.getRemoteAddr(),1);

        return ResponseEntity.ok(new DownloadCodeResponseControllerDto(code));
    }

    @GetMapping("{downloadCode}")
    public ResponseEntity<FileInfoResponseControllerDto> getFileInfo
            (@PathVariable String downloadCode,
             @UserCheck(isLimit = false) HttpServletRequest request) {

        FileInfoResponseControllerDto fileInfoResponseDto = fileInfoService.findByCode(downloadCode).toControllerDto();
        logger.info("{}",fileInfoResponseDto.toString());
        return ResponseEntity.ok(fileInfoResponseDto);
    }

    @DeleteMapping("{downloadCode}")
    public ResponseEntity<MessageDto> deleteFile
            (@PathVariable String downloadCode,
             @UserCheck(isLimit = false) HttpServletRequest request) {

        FileInfoResponseControllerDto fileInfoResponseDto = fileInfoService.revoke(downloadCode);
        fileUtils.delete(fileInfoResponseDto.getFileName());
        userService.addFileCnt(request.getRemoteAddr(),-1);

        return ResponseEntity.ok(new MessageDto("성공적으로 코드와 파일을 삭제했습니다."));
    }

    @GetMapping("{downloadCode}/download")
    public ResponseEntity<Resource> download
            (@PathVariable String downloadCode,
             @UserCheck(isLimit = false) HttpServletRequest request) throws IOException {

        FileInfoResponseServiceDto fileInfoResponseDto = fileInfoService.findByCode(downloadCode);

        File file = fileUtils.find(fileInfoResponseDto.getFileName());

        if(!file.exists()) throw new SigongException(ErrorCode.NOT_FOUND_FILE);

        File targetFile = fileUtils.convertOrigin(file, fileInfoResponseDto.getOriginFileName());


        //file 다운로드용 header 처리
        HttpHeaders header = new HttpHeaders();
        String fileName = URLEncoder.encode(targetFile.getName()).replaceAll("\\+", "%20");
        header.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment;filename*=\"" + fileName+ "\";");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        header.add("charset","utf8");

        Resource resource = new UrlResource("file:"+targetFile.getPath());
        fileInfoService.revoke(downloadCode);

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(targetFile.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}
