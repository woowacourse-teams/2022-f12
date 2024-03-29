package com.woowacourse.f12.presentation.product;

import static com.woowacourse.f12.domain.product.Category.*;

import com.woowacourse.f12.domain.product.Category;
import com.woowacourse.f12.exception.badrequest.InvalidCategoryValueException;
import com.woowacourse.f12.presentation.ViewConstant;
import java.util.Arrays;

public enum CategoryConstant implements ViewConstant {

    KEYBOARD_CONSTANT("keyboard", KEYBOARD),
    MOUSE_CONSTANT("mouse", MOUSE),
    MONITOR_CONSTANT("monitor", MONITOR),
    STAND_CONSTANT("stand", STAND),
    SOFTWARE_CONSTANT("software", SOFTWARE);

    private final String viewValue;
    private final Category category;

    CategoryConstant(final String viewValue, final Category category) {
        this.viewValue = viewValue;
        this.category = category;
    }

    public static CategoryConstant findByViewValue(final String viewValue) {
        return Arrays.stream(values())
                .filter(category -> category.hasViewValue(viewValue))
                .findAny()
                .orElseThrow(InvalidCategoryValueException::new);
    }

    public static CategoryConstant from(final Category category) {
        return Arrays.stream(values())
                .filter(constant -> constant.category.equals(category))
                .findFirst()
                .orElseThrow(InvalidCategoryValueException::new);
    }

    public Category toCategory() {
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
