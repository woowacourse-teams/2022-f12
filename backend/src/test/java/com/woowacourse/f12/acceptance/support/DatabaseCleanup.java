package com.woowacourse.f12.acceptance.support;

import static com.woowacourse.f12.acceptance.support.EscapeSqlUtil.escapeTableName;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.transaction.Transactional;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleanup implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        tableNames = entityManager.getMetamodel().getEntities().stream()
                .filter(it ->
                        it.getJavaType().getDeclaredAnnotation(Table.class) != null)
                .map(it -> it.getJavaType().getDeclaredAnnotation(Table.class).name())
                .collect(Collectors.toList());
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + escapeTableName(tableName)).executeUpdate();
            entityManager.createNativeQuery(
                            "ALTER TABLE " + escapeTableName(tableName) + " ALTER COLUMN ID RESTART WITH 1")
                    .executeUpdate();
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }
}
