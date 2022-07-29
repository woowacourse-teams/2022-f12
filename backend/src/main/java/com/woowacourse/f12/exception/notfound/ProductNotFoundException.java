package com.woowacourse.f12.exception.notfound;

public class ProductNotFoundException extends NotFoundException {

    public ProductNotFoundException() {
        super("제품을 찾을 수 없습니다.");
    }
}
