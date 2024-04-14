package org.ssafy.demotravel.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode{

    InvalidSidoCode(HttpStatus.NOT_FOUND, "유효하지 않은 시도코드입니다."),
    InvalidGugunCode(HttpStatus.NOT_FOUND, "유효하지 않은 구군코드입니다."),
    InvalidKeyword(HttpStatus.NOT_FOUND, "유효하지 않은 키워드입니다."),
    InvalidTravelId(HttpStatus.NOT_FOUND, "유효하지 않은 여행지 ID 입니다."),
    InvalidCoordinate(HttpStatus.NOT_FOUND, "유효하지 않은 좌표 정보입니다."),
    UserCannotAllowed(HttpStatus.FORBIDDEN, "권한이 없는 유저정보입니다.");


    private final HttpStatus httpStatus;
    private final String message;

}
