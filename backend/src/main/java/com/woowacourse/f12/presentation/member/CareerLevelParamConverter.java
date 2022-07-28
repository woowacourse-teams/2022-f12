package com.woowacourse.f12.presentation.member;


import com.woowacourse.f12.dto.CareerLevelConstant;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CareerLevelParamConverter implements Converter<String, CareerLevelConstant> {

    @Override
    public CareerLevelConstant convert(String source) {
        return CareerLevelConstant.findByViewValue(source);
    }
}
