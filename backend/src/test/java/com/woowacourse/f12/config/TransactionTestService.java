package com.woowacourse.f12.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionTestService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public String readOnly() throws SQLException {
        return getDataSourceUrl();
    }

    @Transactional
    public String notReadOnly() throws SQLException {
        return getDataSourceUrl();
    }

    private String getDataSourceUrl() throws SQLException {
        final EntityManagerFactoryInfo entityManagerFactory = (EntityManagerFactoryInfo) entityManager.getEntityManagerFactory();
        final DataSource dataSource = entityManagerFactory.getDataSource();
        try (Connection connection = Objects.requireNonNull(dataSource).getConnection()) {
            return connection.getMetaData().getURL();
        }
    }
}
