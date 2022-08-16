package com.woowacourse.f12.config;

import com.woowacourse.f12.support.RequestLogTimer;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public RequestLogTimer requestLogTimer() {
        return new RequestLogTimer(clock());
    }
}
