package com.woowacourse.f12.logging;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiQueryCounterAspectTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void dataSource_의_getConnection_을_실행하면_프록시_객체를_반환한다() throws Exception {
        // given
        try (Connection connection = dataSource.getConnection()) {
            // when, then
            assertThat(connection).isInstanceOf(Proxy.class);
        }
    }
}
