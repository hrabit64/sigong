package com.hrabit64.sigong.dto.general.controllerDto;

import lombok.*;
import org.springframework.http.HttpStatus;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionMessageDto {
    private HttpStatus status;
    private String title;
    private String detail;
}
