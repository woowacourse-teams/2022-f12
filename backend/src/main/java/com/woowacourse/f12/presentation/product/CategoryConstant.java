package com.woowacourse.f12.presentation.product;

import com.woowacourse.f12.domain.product.Category;
import com.woowacourse.f12.exception.badrequest.InvalidCategoryValueException;
import com.woowacourse.f12.presentation.ViewConstant;
import java.util.Arrays;

public enum CategoryConstant implements ViewConstant {
    KEYBOARD("keyboard", Category.KEYBOARD),
    MOUSE("mouse", Category.MOUSE),
    MONITOR("monitor", Category.MONITOR),
    STAND("stand", Category.STAND),
    SOFTWARE("software", Category.SOFTWARE);

    private final String viewValue;
    private final Category category;

    CategoryConstant(final String viewValue, final Category category) {
        this.viewValue = viewValue;
        this.category = category;
    }

    public static CategoryConstant findByViewValue(final String viewValue) {
        return Arrays.stream(values())
                .filter(category -> category.hasViewValue(viewValue))
                .findFirst()
                .orElseThrow(InvalidCategoryValueException::new);
    }

    public static CategoryConstant fromDomain(final Category category) {
        return Arrays.stream(values())
                .filter(constant -> constant.category.equals(category))
                .findFirst()
                .orElseThrow(InvalidCategoryValueException::new);
    }

    public Category toDomain() {
        return this.category;
    }

    public boolean hasViewValue(final String viewValue) {
        return this.viewValue.equals(viewValue);
    }

    @Override
    public String getViewValue() {
        return viewValue;
    }
}
