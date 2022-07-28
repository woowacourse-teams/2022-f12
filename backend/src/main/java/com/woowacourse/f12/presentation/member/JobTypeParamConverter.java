package com.woowacourse.f12.presentation.member;

import com.woowacourse.f12.dto.JobTypeConstant;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class JobTypeParamConverter implements Converter<String, JobTypeConstant> {

    @Override
    public JobTypeConstant convert(String source) {
        return JobTypeConstant.findByViewValue(source);
    }
}
