package com.woowacourse.f12.presentation.product;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryParamConverter implements Converter<String, CategoryConstant> {

    @Override
    public CategoryConstant convert(String source) {
        return CategoryConstant.findByViewValue(source);
    }
}
