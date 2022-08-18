package com.woowacourse.f12.exception;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCode {

    INVALID_SEARCH_PARAM("40000"),
    INVALID_PAGING_PARAM("40001"),
    INVALID_LOGIN_CODE("40002"),
    INVALID_REQUEST_BODY_TYPE("40003"),

    INVALID_MEMBER_INFO_VALUE("40010"),
    EMPTY_MEMBER_INFO_VALUE("40011"),

    INVALID_REVIEW_RATING("40030"),
    BLANK_REVIEW_CONTENT("40031"),
    INVALID_REVIEW_CONTENT_LENGTH("40032"),
    ALREADY_WRITTEN_REVIEW("40033"),

    INVALID_CATEGORY_PROFILE_PRODUCT("40040"),
    CATEGORY_DUPLICATED_PROFILE_PRODUCT("40041"),
    EMPTY_PROFILE_PRODUCT("40042"),
    INVALID_PROFILE_PRODUCT_UPDATE("40043"),

    SELF_FOLLOW("40050"),
    ALREADY_FOLLOWING("40051"),
    NOT_FOLLOWING("40052"),

    NOT_EXIST_TOKEN("40100"),
    EXPIRED_TOKEN("40101"),
    INVALID_TOKEN("40102"),
    REGISTER_NOT_COMPLETED("40103"),

    PERMISSION_DENIED("40300"),

    MEMBER_NOT_FOUND("40410"),

    PRODUCT_NOT_FOUND("40420"),

    REVIEW_NOT_FOUND("40430"),

    INVENTORY_PRODUCT_NOT_FOUND("40440"),

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
