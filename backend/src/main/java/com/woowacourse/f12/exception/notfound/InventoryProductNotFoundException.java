package com.woowacourse.f12.exception.notfound;

import static com.woowacourse.f12.exception.ErrorCode.DATA_NOT_FOUND;

public class InventoryProductNotFoundException extends NotFoundException {

    public InventoryProductNotFoundException() {
        super("등록 장비를 찾을 수 없습니다.", DATA_NOT_FOUND);
    }
}
