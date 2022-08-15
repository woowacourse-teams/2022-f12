package com.woowacourse.f12.config;

import com.woowacourse.f12.logging.QueryInspector;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig {

    private final QueryInspector queryInspector;

    public HibernateConfig(final QueryInspector queryInspector) {
        this.queryInspector = queryInspector;
    }

    @Bean
    public HibernatePropertiesCustomizer configureStatementInspector() {
        return hibernateProperties ->
                hibernateProperties.put(AvailableSettings.STATEMENT_INSPECTOR, queryInspector);
    }
}
