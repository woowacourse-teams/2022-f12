package com.woowacourse.f12.logging;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ApiQueryInspectorTest {

    @Test
    void inspect는_실행될_sql문을_그대로_반환한다() {
        // given
        ApiQueryInspector apiQueryInspector = new ApiQueryInspector(new QueryCounter());

        // when
        String inspectResult = apiQueryInspector.inspect("sql");

        // then
        assertThat(inspectResult).isEqualTo("sql");
    }

    @Test
    void inspect가_실행되면_QueryCounter의_count를_증가시킨다() {
        // given
        ApiQueryInspector apiQueryInspector = new ApiQueryInspector(new QueryCounter());

        // when
        apiQueryInspector.inspect("sql");

        // then
        assertThat(apiQueryInspector.getQueryCount()).isEqualTo(1);
    }
}
