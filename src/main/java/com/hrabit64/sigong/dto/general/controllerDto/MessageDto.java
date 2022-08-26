package com.hrabit64.sigong.dto.general.controllerDto;

import lombok.*;
import org.springframework.http.HttpStatus;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {

    private String message;
}
