package com.woowacourse.f12.dto.request;

import lombok.Getter;

@Getter
public class ProfileProductRequest {

    private Long selectedInventoryProductId;
    private Long unselectedInventoryProductId;

    private ProfileProductRequest() {
    }

    public ProfileProductRequest(final Long selectedInventoryProductId, final Long unselectedInventoryProductId) {
        this.selectedInventoryProductId = selectedInventoryProductId;
        this.unselectedInventoryProductId = unselectedInventoryProductId;
    }
}
