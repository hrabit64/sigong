package com.hrabit64.sigong.utils.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FileTypeValidator.class)
@Target( { ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface FileTypeCheck {

    String message() default "업로드할 수 없는 파일 형식입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
