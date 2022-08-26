package com.hrabit64.sigong.utils.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.Objects;

import static com.hrabit64.sigong.config.SigongConfig.AllowFileTypes;


public class FileTypeValidator implements ConstraintValidator<FileTypeCheck, MultipartFile> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        logger.debug("type : {}",Objects.requireNonNull(value.getOriginalFilename()).substring(value.getOriginalFilename().lastIndexOf(".")+1));
        return AllowFileTypes.contains(Objects.requireNonNull(value.getOriginalFilename()).substring(value.getOriginalFilename().lastIndexOf(".")+1));
    }
}
