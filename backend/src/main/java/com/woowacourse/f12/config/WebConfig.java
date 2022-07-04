package com.woowacourse.f12.config;

import com.woowacourse.f12.presentation.CustomPageableHandlerArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        CustomPageableHandlerArgumentResolver customPageableHandlerArgumentResolver = new CustomPageableHandlerArgumentResolver();
        customPageableHandlerArgumentResolver.setMaxPageSize(150);
        resolvers.add(customPageableHandlerArgumentResolver);
    }
}
