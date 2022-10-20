package com.woowacourse.f12.logging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class ConnectionInvocationHandlerTest {

    @Mock
    private ApiQueryCounter apiQueryCounter;

    @BeforeEach
    void setUp() {
        this.apiQueryCounter = mock(ApiQueryCounter.class);
    }

    @Test
    void 실행시킬_메서드가_prepareStatement_메서드일_경우_프록시_객체를_반환한다() throws Exception {
        // given
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test")) {
            ConnectionInvocationHandler connectionInvocationHandler = new ConnectionInvocationHandler(connection,
                    apiQueryCounter);
            Method prepareStatementMethod = connection.getClass().getMethod("prepareStatement", String.class);

            // when
            Object actual = connectionInvocationHandler.invoke(null, prepareStatementMethod,
                    new Object[]{"CREATE TABLE a(id int)"});

            // then
            assertThat(actual).isInstanceOf(Proxy.class);
        }
    }

    @Test
    void 실행시킬_메서드가_prepareStatement_메서드가_아닐_경우_기존_객체를_반환한다() throws Exception {
        // given
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test")) {
            ConnectionInvocationHandler connectionInvocationHandler = new ConnectionInvocationHandler(connection,
                    apiQueryCounter);
            Method method = connection.getClass().getMethod("isReadOnly");

            // when
            Object actual = connectionInvocationHandler.invoke(null, method, null);

            // then
            assertThat(actual).isNotInstanceOf(Proxy.class);
        }
    }
}
