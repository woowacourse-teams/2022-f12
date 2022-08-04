package com.woowacourse.f12.dto.response;

import com.woowacourse.f12.exception.CustomException;
import com.woowacourse.f12.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ExceptionResponse {

    private String message;
    private ErrorCode errorCode;

    private ExceptionResponse() {
    }

    private ExceptionResponse(final String message, final ErrorCode errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public static ExceptionResponse from(final CustomException e) {
        return new ExceptionResponse(e.getMessage(), e.getErrorCode());
    }

    public static ExceptionResponse from(final String message, final ErrorCode errorCode) {
        return new ExceptionResponse(message, errorCode);
    }
}
