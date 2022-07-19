package com.woowacourse.f12.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String CORS_ALLOWED_METHODS = "GET,POST,HEAD,PUT,PATCH,DELETE,TRACE,OPTIONS";

    private final List<HandlerInterceptor> interceptors;
    private final List<HandlerMethodArgumentResolver> resolvers;

    public WebConfig(List<HandlerInterceptor> interceptors, List<HandlerMethodArgumentResolver> resolvers) {
        this.interceptors = interceptors;
        this.resolvers = resolvers;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        interceptors.forEach(registry::addInterceptor);
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedMethods(CORS_ALLOWED_METHODS.split(","))
                .exposedHeaders(HttpHeaders.LOCATION);
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.addAll(this.resolvers);
    }
}
