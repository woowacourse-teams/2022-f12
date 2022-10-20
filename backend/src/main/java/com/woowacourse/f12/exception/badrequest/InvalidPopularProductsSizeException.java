package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_SEARCH_PARAM;

public class InvalidPopularProductsSizeException extends InvalidValueException {

    public InvalidPopularProductsSizeException(final int minSize, final int maxSize) {
        super(INVALID_SEARCH_PARAM, String.format("인기 제품의 개수는 %d 이상, %d 이하여야 합니다.", minSize, maxSize));
    }
}
