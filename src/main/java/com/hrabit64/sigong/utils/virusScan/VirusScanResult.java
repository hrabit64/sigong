package com.hrabit64.sigong.utils.virusScan;

import lombok.*;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VirusScanResult {
    private String md5;
    private String sha1;
    private String sha256;
    private String virusScanResultLink;
}
