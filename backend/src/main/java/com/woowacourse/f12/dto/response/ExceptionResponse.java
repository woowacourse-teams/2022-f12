package com.woowacourse.f12.dto.response;

import lombok.Getter;

@Getter
public class ExceptionResponse {

    private String message;

    private ExceptionResponse() {
    }

    private ExceptionResponse(final String message) {
        this.message = message;
    }

    public static ExceptionResponse from(final Exception e) {
        return new ExceptionResponse(e.getMessage());
    }
}
