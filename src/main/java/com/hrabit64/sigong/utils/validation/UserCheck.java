package com.hrabit64.sigong.utils.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserValidator.class)
@Target( { ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserCheck {

    String message() default "현재 해당 서비스를 사용할 수 없는 상태입니다! 잠시후에 다시 시도해주세요";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean isLimit();
}
