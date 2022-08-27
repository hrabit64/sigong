package com.hrabit64.sigong.utils.validation;

import com.hrabit64.sigong.exception.ErrorCode;
import com.hrabit64.sigong.exception.SigongException;
import com.hrabit64.sigong.utils.user.UserUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

import static com.hrabit64.sigong.config.SigongConfig.AllowFileTypes;

@RequiredArgsConstructor
public class UserValidator implements ConstraintValidator<UserCheck, HttpServletRequest> {
    private final UserUtils userUtils;
    private boolean isLimit;

    @Override
    public void initialize(UserCheck constraintAnnotation) {
        isLimit = constraintAnnotation.isLimit();
    }

    @Override
    public boolean isValid(HttpServletRequest value, ConstraintValidatorContext context) {
        try{
         userUtils.checkValid(value.getRemoteAddr());
        }catch (SigongException ex){
            if(ex.getErrorCode() == ErrorCode.YOUR_BAN ) return false;
            if(ex.getErrorCode() == ErrorCode.OVER_LIMIT_CNT && isLimit) return false;
        }

        return true;
    }
}
