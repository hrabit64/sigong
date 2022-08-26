package com.hrabit64.sigong.exception;

import lombok.Getter;
import java.io.File;

@Getter
public class VirusScanException extends RuntimeException {

    private final File errorFile;

    public VirusScanException(File file){
        this.errorFile = file;
    }
}