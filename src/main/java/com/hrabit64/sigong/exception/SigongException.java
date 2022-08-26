package com.hrabit64.sigong.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SigongException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public SigongException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.message = "";
    }
}