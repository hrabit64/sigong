package com.hrabit64.sigong.dto.file.serviceDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hrabit64.sigong.domain.file.FileInfo;
import com.hrabit64.sigong.dto.file.controllerDto.FileInfoResponseControllerDto;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FileInfoResponseServiceDto {

    private String originFileName;
    private String fileName;
    private String md5;
    private String sha1;
    private String sha256;
    private String virusScanResultLink;
    private LocalDateTime createdDateTime;
    private String ip;

    public FileInfoResponseServiceDto(FileInfo file){
        originFileName = file.getOriginFileName();
        fileName = file.getFileName();
        md5 = file.getMd5();
        sha1 = file.getSha1();
        sha256 = file.getSha256();
        virusScanResultLink = file.getVirusScanResultLink();
        createdDateTime = file.getCreatedDateTime();
        ip = file.getIp();
    }

    public FileInfoResponseControllerDto toControllerDto(){
        return FileInfoResponseControllerDto.builder()
                .createdDateTime(createdDateTime)
                .fileName(originFileName)
                .md5(md5)
                .sha1(sha1)
                .sha256(sha256)
                .virusScanResultLink(virusScanResultLink)
                .build();
    }
}
