package com.hrabit64.sigong.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Sigong's main config class.
 *
 * @author hrabit64
 * @version 0.0.1-SNAPSHOT
 * @since 0.0.1-SNAPSHOT
 */

@Configuration
public class SigongConfig {

    /**
     * allow file types(=file type whitelist)
     * 허용된 파일들의 확장자명입니다.
     * 해당 확장자명들 외 모든 파일 업로드 요청은 거부합니다.
     */
    public static List<String> AllowFileTypes = Arrays.asList(
            "pdf",
            "ppt",
            "pptx",
            "hwp",
            "csv",
            "xlsx"
    );

    /**
     * IP ban time(minute)
     * 해당 IP를 벤 처리했을 때, 얼마나 벤을 유지시킬지 설정합니다.
     * 이 서비스에서는 바이러스 의심 파일을 업로드 할경우 해당 IP를 자동으로 차단합니다.
     * 이때 해당 유저는 banTime 만큼 업로드 및 다운로드 서비스로부터 차단당하며, 벤 메시지를 표시합니다.
     */
    public static Long BanTime = 20L;

    public static Integer MaxFileCnt = 3;

    /**
     * CodeExpireTime
     * 발급된 코드가 몇분 지속될지 설정합니다.
     */
    public static Long CodeExpireTime = 10L;

}
