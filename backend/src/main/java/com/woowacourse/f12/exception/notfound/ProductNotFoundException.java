package com.woowacourse.f12.exception.notfound;

import static com.woowacourse.f12.exception.ErrorCode.DATA_NOT_FOUND;

public class ProductNotFoundException extends NotFoundException {

    public ProductNotFoundException() {
        super("제품을 찾을 수 없습니다.", DATA_NOT_FOUND);
    }
}
