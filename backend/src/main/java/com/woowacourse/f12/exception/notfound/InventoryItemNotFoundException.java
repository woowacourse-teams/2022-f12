package com.woowacourse.f12.exception.notfound;

public class InventoryItemNotFoundException extends NotFoundException {

    public InventoryItemNotFoundException() {
        super("등록 장비를 찾을 수 없습니다.");
    }
}
