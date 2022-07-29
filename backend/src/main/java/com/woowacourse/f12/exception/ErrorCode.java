package com.woowacourse.f12.exception;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCode {

    INVALID_SEARCH_PARAM("40000"),
    INVALID_REQUEST_BODY("40001"),
    INVALID_TOKEN("40002"),
    DUPLICATED_CONTENT("40003"),
    BLANK_CONTENT("40004"),
    NOT_ENOUGH_DATA("40005"),

    TOKEN_NOT_EXISTS("40100"),
    TOKEN_EXPIRED("400101"),
    TOKEN_INVALID("400102"),

    PERMISSION_DENIED("40300"),

    DATA_NOT_FOUND("40400"),

    INTERNAL_SERVER_ERROR("50000"),
    EXTERNAL_SERVER_ERROR("50001");

    private final String value;

    ErrorCode(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
