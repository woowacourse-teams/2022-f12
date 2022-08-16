package com.woowacourse.f12.config;

import com.woowacourse.f12.logging.ApiQueryInspector;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig {

    private final ApiQueryInspector apiQueryInspector;

    public HibernateConfig(final ApiQueryInspector apiQueryInspector) {
        this.apiQueryInspector = apiQueryInspector;
    }

    @Bean
    public HibernatePropertiesCustomizer configureStatementInspector() {
        return hibernateProperties ->
                hibernateProperties.put(AvailableSettings.STATEMENT_INSPECTOR, apiQueryInspector);
    }
}
