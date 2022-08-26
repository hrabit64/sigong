package com.hrabit64.sigong.dto.file.controllerDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hrabit64.sigong.domain.file.FileInfo;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FileInfoResponseControllerDto {

    private String fileName;
    private String md5;
    private String sha1;
    private String sha256;
    private String virusScanResultLink;
    private LocalDateTime createdDateTime;

    public FileInfoResponseControllerDto(FileInfo file){
        fileName = file.getOriginFileName();
        md5 = file.getMd5();
        sha1 = file.getSha1();
        sha256 = file.getSha256();
        virusScanResultLink = file.getVirusScanResultLink();
        createdDateTime = file.getCreatedDateTime();
    }

}
