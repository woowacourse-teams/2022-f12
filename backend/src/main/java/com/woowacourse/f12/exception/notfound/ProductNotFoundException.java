package com.woowacourse.f12.exception.notfound;

import static com.woowacourse.f12.exception.ExceptionCode.DATA_NOT_FOUND;

public class ProductNotFoundException extends NotFoundException {

    public ProductNotFoundException() {
        super(DATA_NOT_FOUND, "제품을 찾을 수 없습니다.");
    }
}
