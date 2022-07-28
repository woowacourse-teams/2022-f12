package com.woowacourse.f12.presentation;

import com.fasterxml.jackson.annotation.JsonValue;

public interface ViewConstant {

    @JsonValue
    String getViewValue();
}
