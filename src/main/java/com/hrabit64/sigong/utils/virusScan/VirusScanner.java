package com.hrabit64.sigong.utils.virusScan;

import com.hrabit64.sigong.exception.SigongException;
import com.hrabit64.sigong.exception.VirusScanException;
import com.kanishka.virustotal.dto.FileScanReport;
import com.kanishka.virustotal.dto.ScanInfo;
import com.kanishka.virustotal.exception.APIKeyNotFoundException;
import com.kanishka.virustotal.exception.QuotaExceededException;
import com.kanishka.virustotal.exception.UnauthorizedAccessException;
import com.kanishka.virustotalv2.VirustotalPublicV2;
import com.kanishka.virustotalv2.VirustotalPublicV2Impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * Virus Total API를 사용하여 바이러스 검사를 수행하는 class 입니다.
 *
 * @author hrabit64
 * @version 0.0.1-SNAPSHOT
 * @since 0.0.1-SNAPSHOT
 */
@Component
public class VirusScanner {

    private final VirustotalPublicV2 virusTotalRef;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public VirusScanner() throws APIKeyNotFoundException {
        virusTotalRef = new VirustotalPublicV2Impl();

    }

    /**
     * 입력으로 받은 File을 Virus Total에 업로드하여 검사 후, 단 한건이라도 감지 된다면,
     * VIRUS_SCAN_DETECT 에러를 발생시킵니다.
     *
     * @author hrabit64
     * @since 0.0.1-SNAPSHOT
     */
    public VirusScanResult scanFile(File file) throws UnauthorizedAccessException, IOException, QuotaExceededException {

        //Virus Total에 검사할 파일 업로드
        ScanInfo scanInformation = virusTotalRef.scanFile(file);

        //Virus Total에 업로드한 파일 검사 결과 가져오기
        FileScanReport report = virusTotalRef.getScanReport(scanInformation.getResource());
        logger.debug("fin1");
        //결과를 확인하여, 단 한건이라도 감지 됐다면 오류 발생,
        report.getScans().forEach((s, virusScanInfo) -> {
            if(virusScanInfo.isDetected()) throw new VirusScanException(file);
        });

        logger.debug("fin2");

        return VirusScanResult.builder()
                .md5(report.getMd5())
                .sha1(report.getSha1())
                .sha256(report.getSha256())
                .virusScanResultLink(report.getPermalink())
                .build();
    }
}
