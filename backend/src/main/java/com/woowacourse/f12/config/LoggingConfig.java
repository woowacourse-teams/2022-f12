package com.woowacourse.f12.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class LoggingConfig {

    @Bean
    @RequestScope
    public StopWatch stopWatch() {
        return new StopWatch();
    }
}
