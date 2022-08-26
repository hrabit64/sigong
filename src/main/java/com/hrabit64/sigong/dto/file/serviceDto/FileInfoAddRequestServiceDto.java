package com.hrabit64.sigong.dto.file.serviceDto;

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
public class FileInfoAddRequestServiceDto {
    private String code;
    private String fileName;
    private String originFileName;
    private String md5;
    private String sha1;
    private String sha256;
    private String virusScanResultLink;
    private String ip;

    public FileInfo toEntity(Long ttl){
        return FileInfo.builder()
                .code(code)
                .fileName(fileName)
                .originFileName(originFileName)
                .md5(md5)
                .sha1(sha1)
                .sha256(sha256)
                .virusScanResultLink(virusScanResultLink)
                .createdDateTime(LocalDateTime.now())
                .ip(ip)
                .ttl(ttl)
                .build();
    }
    @Override
    public boolean equals(Object o){
        return this.toString().equals(o.toString());
    }
}
