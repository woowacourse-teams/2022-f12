package com.woowacourse.f12.exception.notfound;

import static com.woowacourse.f12.exception.ErrorCode.INVENTORY_PRODUCT_NOT_FOUND;

public class InventoryProductNotFoundException extends NotFoundException {

    public InventoryProductNotFoundException() {
        super(INVENTORY_PRODUCT_NOT_FOUND, "등록 장비를 찾을 수 없습니다.");
    }
}
