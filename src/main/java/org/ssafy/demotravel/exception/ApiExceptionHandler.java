package org.ssafy.demotravel.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(CustomErrorException.class)
    protected ResponseEntity<ErrorResource> handleCustomError(CustomErrorException e) {
        ErrorResource errorResource = new ErrorResource(e.getErrorCode());
        return new ResponseEntity<>(errorResource, errorResource.getContent().getHttpStatus());
    }
}
