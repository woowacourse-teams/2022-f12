package com.woowacourse.f12.logging;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class QueryInspectorTest {

    @Test
    void inspect는_실행될_sql문을_그대로_반환한다() {
        // given
        QueryInspector queryInspector = new QueryInspector(new QueryCounter());

        // when
        String inspectResult = queryInspector.inspect("sql");

        // then
        assertThat(inspectResult).isEqualTo("sql");
    }

    @Test
    void inspect가_실행되면_QueryCounter의_count를_증가시킨다() {
        // given
        QueryInspector queryInspector = new QueryInspector(new QueryCounter());

        // when
        queryInspector.inspect("sql");

        // then
        assertThat(queryInspector.getQueryCount()).isEqualTo(1);
    }
}
