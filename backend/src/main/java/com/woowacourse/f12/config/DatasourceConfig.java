package com.woowacourse.f12.config;

import static com.woowacourse.f12.support.DataSourceType.MASTER;
import static com.woowacourse.f12.support.DataSourceType.SLAVE;

import com.woowacourse.f12.support.ReplicationRoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Profile({"main", "release", "performance"})
public class DatasourceConfig {

    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "slaveDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.slave")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
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
        return bean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory entityManagerFactoryBean) {
        return new JpaTransactionManager(entityManagerFactoryBean);
    }
}
