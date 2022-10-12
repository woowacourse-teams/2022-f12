package com.woowacourse.f12.config;

import static com.woowacourse.f12.support.DataSourceType.MASTER;
import static com.woowacourse.f12.support.DataSourceType.SLAVE;

import com.woowacourse.f12.support.ReplicationRoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@TestConfiguration
public class TestDataSourceConfig {

    @Bean(name = "masterDataSource")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url("jdbc:h2:~/master")
                .username("sa")
                .password("")
                .build();
    }

    @Bean(name = "slaveDataSource")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url("jdbc:h2:~/slave")
                .username("sa")
                .password("")
                .build();
    }

    @Bean(name = "routeDataSource")
    public DataSource routeDataSource(@Qualifier("masterDataSource") final DataSource masterDataSource,
                                      @Qualifier("slaveDataSource") final DataSource slaveDataSource) {
        final ReplicationRoutingDataSource replicationRoutingDataSource = new ReplicationRoutingDataSource();
        final Map<Object, Object> dataSources = Map.of(MASTER, masterDataSource, SLAVE, slaveDataSource);
        replicationRoutingDataSource.setTargetDataSources(dataSources);
        replicationRoutingDataSource.setDefaultTargetDataSource(masterDataSource);
        return replicationRoutingDataSource;
    }

    @Primary
    @Bean
    public DataSource dataSource(@Qualifier("routeDataSource") final DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(final DataSource dataSource) {
        final LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPackagesToScan("com.woowacourse.f12");
        final HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        bean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.format_sql", true);
        bean.setJpaPropertyMap(properties);
        return bean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory entityManagerFactoryBean) {
        return new JpaTransactionManager(entityManagerFactoryBean);
    }
}
