package com.woowacourse.f12.logging;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@SpringBootTest
class ApiQueryInspectorTest {

    @Autowired
    private ApiQueryInspector apiQueryInspector;

    @Autowired
    private ApiQueryCounter apiQueryCounter;

    @Test
    void inspect는_원래_쿼리를_그대로_반환한다() {
        // given, when
        String inspectResult = apiQueryInspector.inspect("sql");

        // then
        assertThat(inspectResult).isEqualTo("sql");
    }

    @Test
    void request_안에서_inspect를_호출하면_QueryCounter를_증가시킨다() {
        // given
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));

        // when
        apiQueryInspector.inspect("sql");

        // then
        assertThat(apiQueryCounter.getCount()).isOne();
    }
}
