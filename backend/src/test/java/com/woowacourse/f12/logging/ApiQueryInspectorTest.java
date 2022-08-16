package com.woowacourse.f12.logging;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void setUp() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @Test
    void inspect는_실행될_sql문을_그대로_반환한다() {
        // given, when
        String inspectResult = apiQueryInspector.inspect("sql");

        // then
        assertThat(inspectResult).isEqualTo("sql");
    }

    @Test
    void inspect가_실행되면_QueryCounter의_count를_증가시킨다() {
        // given, when
        apiQueryInspector.inspect("sql");

        // then
        assertThat(apiQueryInspector.getQueryCount()).isEqualTo(1);
    }
}
