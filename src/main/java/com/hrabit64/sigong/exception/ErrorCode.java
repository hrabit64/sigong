package com.hrabit64.sigong.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //400
    INVALID_FILE_CODE(HttpStatus.NOT_FOUND,"INVALID_FILE_CODE","해당 코드에 대한 파일을 찾을 수 없습니다!"),

    //403
    OVER_LIMIT_CNT(HttpStatus.FORBIDDEN,"OVER_LIMIT_CNT","최대 업로드 수에 도달했습니다. 잠시 뒤에 시도해주세요"),
    YOUR_BAN(HttpStatus.FORBIDDEN,"YOUR_BAN","당신은 해당 서비스에 접근이 금지되었습니다. 서비스 관리자에게 문의하세요"),

    //500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"INTERNAL_SERVER_ERROR","알 수 없는 서버 에러입니다."),
    NOT_FOUND_FILE(HttpStatus.INTERNAL_SERVER_ERROR,"NOT_FOUND_FILE","해당 코드는 유효하나, 서버 오류로 해당 코드에 대한 파일을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String title;
    private final String detail;
}
