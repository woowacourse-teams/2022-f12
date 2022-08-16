package com.woowacourse.f12.logging;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Getter
@Component
@RequestScope
public class QueryCounter {

    private int count;

    public void increaseCount() {
        count++;
    }
}
