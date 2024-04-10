package org.ssafy.demotravel.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomErrorException extends RuntimeException{
    private final ErrorCode errorCode;
}
