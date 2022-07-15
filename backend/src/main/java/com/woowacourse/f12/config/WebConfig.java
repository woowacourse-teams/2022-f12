package com.woowacourse.f12.config;

import com.woowacourse.f12.presentation.AuthArgumentResolver;
import com.woowacourse.f12.presentation.AuthInterceptor;
import com.woowacourse.f12.presentation.CustomPageableArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String CORS_ALLOWED_METHODS = "GET,POST,HEAD,PUT,PATCH,DELETE,TRACE,OPTIONS";

    private final AuthInterceptor authInterceptor;
    private final AuthArgumentResolver authArgumentResolver;

    public WebConfig(final AuthInterceptor authInterceptor, final AuthArgumentResolver authArgumentResolver) {
        this.authInterceptor = authInterceptor;
        this.authArgumentResolver = authArgumentResolver;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedMethods(CORS_ALLOWED_METHODS.split(","))
                .exposedHeaders(HttpHeaders.LOCATION);
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        final CustomPageableArgumentResolver customPageableArgumentResolver = new CustomPageableArgumentResolver();
        customPageableArgumentResolver.setMaxPageSize(150);
        resolvers.add(customPageableArgumentResolver);
        resolvers.add(authArgumentResolver);
    }
}
