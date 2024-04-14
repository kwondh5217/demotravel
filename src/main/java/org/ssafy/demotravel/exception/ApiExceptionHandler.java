package org.ssafy.demotravel.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class ApiExceptionHandler {


//    @ExceptionHandler(CustomErrorException.class)
//    protected ResponseEntity<ErrorResource> handleCustomError(CustomErrorException e) {
//        ErrorResource errorResource = new ErrorResource(e.getErrorCode());
//        return new ResponseEntity<>(errorResource, errorResource.getContent().getHttpStatus());
//    }

    @ExceptionHandler(CustomErrorException.class)
    protected ResponseEntity<ErrorResponseDtoResource> handleCustomErrorDto(CustomErrorException e) {
        ErrorResponseDto dto = ErrorResponseDto.builder()
                .status(e.getErrorCode().getHttpStatus())
                .message(e.getErrorCode().getMessage())
                .build();
        ErrorResponseDtoResource errorResponseDtoResource = new ErrorResponseDtoResource(dto);
        return new ResponseEntity<>(errorResponseDtoResource, errorResponseDtoResource.getContent().getStatus());
    }
}
