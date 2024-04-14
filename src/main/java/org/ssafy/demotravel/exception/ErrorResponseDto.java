package org.ssafy.demotravel.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Builder @AllArgsConstructor
@Getter @Setter
public class ErrorResponseDto {

    private HttpStatus status;
    private String message;

}
