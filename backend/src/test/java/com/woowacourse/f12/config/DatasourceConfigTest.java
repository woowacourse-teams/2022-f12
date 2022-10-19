package com.woowacourse.f12.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestDataSourceConfig.class)
class DatasourceConfigTest {

    @Autowired
    private TransactionTestService transactionTestService;

    @Test
    void transaction이_readOnly라면_slave_datasource를_선택한다() throws SQLException {
        String url = transactionTestService.readOnly();

        assertThat(url).isEqualTo("jdbc:h2:mem:slave");
    }

    @Test
    void transaction에_readOnly가_없으면_master_datasource를_선택한다() throws SQLException {
        String url = transactionTestService.notReadOnly();

        assertThat(url).isEqualTo("jdbc:h2:mem:master");
    }
}
