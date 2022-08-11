package com.woowacourse.f12.dto.response;

import com.woowacourse.f12.exception.CustomException;
import com.woowacourse.f12.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class ExceptionResponse {

    private String message;
    private ExceptionCode exceptionCode;

    private ExceptionResponse() {
    }

    private ExceptionResponse(final String message, final ExceptionCode exceptionCode) {
        this.message = message;
        this.exceptionCode = exceptionCode;
    }

    public static ExceptionResponse from(final CustomException e) {
        return new ExceptionResponse(e.getMessage(), e.getExceptionCode());
    }

    public static ExceptionResponse from(final String message, final ExceptionCode exceptionCode) {
        return new ExceptionResponse(message, exceptionCode);
    }
}
