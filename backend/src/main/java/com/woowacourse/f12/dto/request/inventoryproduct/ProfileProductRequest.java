package com.woowacourse.f12.dto.request.inventoryproduct;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProfileProductRequest {

    @NotNull(message = "등록할 대표장비가 없습니다.")
    private List<Long> selectedInventoryProductIds;

    private ProfileProductRequest() {
    }

    public ProfileProductRequest(final List<Long> selectedInventoryProductIds) {
        this.selectedInventoryProductIds = selectedInventoryProductIds;
    }
}
