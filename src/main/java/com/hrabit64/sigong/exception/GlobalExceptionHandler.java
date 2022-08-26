package com.hrabit64.sigong.exception;

import com.hrabit64.sigong.dto.general.controllerDto.ExceptionMessageDto;
import com.kanishka.virustotal.exception.QuotaExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.ConstraintViolationException;
import java.io.UnsupportedEncodingException;

import static com.hrabit64.sigong.exception.ErrorCode.INTERNAL_SERVER_ERROR;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    protected ResponseEntity<ExceptionMessageDto> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception, WebRequest request) {

        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .status(HttpStatus.NOT_FOUND)
                .title("NOT_FOUND")
                .detail("해당 Path를 찾을 수 없습니다.")
                .build()
                , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ MultipartException.class })
    protected ResponseEntity<ExceptionMessageDto> handleMultipartException(MultipartException exception, WebRequest request) {

        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .title("MultipartException")
                .detail("업로드할 수 있는 파일이 아닙니다! 도움말을 다시 확인해주세요!")
                .build()
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ SigongException.class })
    protected ResponseEntity<ExceptionMessageDto> handleCustomException(SigongException exception, WebRequest request) {

        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .status(exception.getErrorCode().getHttpStatus())
                .title(exception.getErrorCode().getTitle())
                .detail(exception.getErrorCode().getDetail() + " " +exception.getMessage())
                .build()
                , exception.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler({ QuotaExceededException.class })
    protected ResponseEntity<ExceptionMessageDto> handleQuotaExceededException(QuotaExceededException exception, WebRequest request) {
        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .status(INTERNAL_SERVER_ERROR.getHttpStatus())
                .title(INTERNAL_SERVER_ERROR.getTitle())
                .detail("바이러스 검사 중 오류가 발생했습니다. 잠시 뒤에 다시 시도해주세요")
                .build()
                ,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    protected ResponseEntity<ExceptionMessageDto> handleConstraintViolationException(ConstraintViolationException exception, WebRequest request) {
        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .title("BAD_REQUEST")
                .detail(exception.getMessage())
                .build()
                ,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ VirusScanException.class })
    protected ResponseEntity<ExceptionMessageDto> handleVirusScanException(VirusScanException exception, WebRequest request) {
        exception.getErrorFile().delete();
        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .status(HttpStatus.FORBIDDEN)
                .title("VIRUS_DETECT")
                .detail("해당 파일에서 바이러스가 검출되었습니다.")
                .build()
                ,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ Exception.class })
    protected ResponseEntity<ExceptionMessageDto> handleServerException(Exception exception, WebRequest request) {
        exception.printStackTrace();
        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .status(INTERNAL_SERVER_ERROR.getHttpStatus())
                .title(INTERNAL_SERVER_ERROR.getTitle())
                .detail(INTERNAL_SERVER_ERROR.getDetail())
                .build()
                ,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ExceptionMessageDto> processMissingServletRequestPartException(MissingServletRequestPartException exception, WebRequest request) {

        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .title("BAD_REQUEST_PARAMS")
                .detail(exception.getMessage())
                .build()
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionMessageDto> processMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception, WebRequest request) {

        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .title("BAD_REQUEST_PARAMS")
                .detail(exception.getMessage())
                .build()
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionMessageDto> processValidationError(MethodArgumentNotValidException exception, WebRequest request) {
        BindingResult bindingResult = exception.getBindingResult();
        StringBuilder builder = new StringBuilder();
        builder.append("잘못된 입력값이 존재합니다! ");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("필드명 : (");
            builder.append(fieldError.getField());
            builder.append(") 오류 메시지: (");
            builder.append(fieldError.getDefaultMessage());
            builder.append(") 입력된 값: ");
            builder.append(fieldError.getRejectedValue());
            builder.append(" // ");
        }

        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .title("METHOD_ARGUMENT_NOT_VALID")
                .detail(builder.toString())
                .build()
                ,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionMessageDto> processHttpMessageNotReadableException(HttpMessageNotReadableException exception, WebRequest request) {

        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .title("HttpMessageNotReadableException")
                .detail("입력 형식이 잘못됬습니다. 다시 확인하세요")
                .build()
                ,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedEncodingException.class)
    public ResponseEntity<ExceptionMessageDto> processUnsupportedEncodingException(UnsupportedEncodingException exception, WebRequest request) {

        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .title("UnsupportedEncodingException")
                .detail("바이러스 검사를 수행할 수 없는 파일입니다! 파일을 다시 확인해주세요!")
                .build()
                ,HttpStatus.BAD_REQUEST);
    }

}