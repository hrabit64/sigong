package com.hrabit64.sigong.domain.file;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@RedisHash("fileinfo")
public class FileInfo {

    @Id
    private String code;

    @Indexed
    private String fileName;
    private String originFileName;
    private String md5;
    private String sha1;
    private String sha256;
    private String virusScanResultLink;
    private LocalDateTime createdDateTime;
    private String ip;

    @TimeToLive(unit = TimeUnit.MINUTES)
    private Long ttl;

    public boolean equalsExcludeTTL(FileInfo fileInfo){
        return fileInfo.getFileName().equals(fileName) &&
                fileInfo.getOriginFileName().equals(originFileName) &&
                fileInfo.getMd5().equals(md5) &&
                fileInfo.getSha1().equals(sha1) &&
                fileInfo.getSha256().equals(sha256) &&
                fileInfo.getVirusScanResultLink().equals(virusScanResultLink) &&
                fileInfo.getCreatedDateTime().equals(createdDateTime) &&
                fileInfo.getIp().equals(ip) &&
                fileInfo.getCode().equals(code);
    }

}
