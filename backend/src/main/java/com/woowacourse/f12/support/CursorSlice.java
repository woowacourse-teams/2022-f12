package com.woowacourse.f12.support;

import java.util.List;

public class CursorSlice<T> {

    private final List<T> content;
    private final boolean hasNext;

    public CursorSlice(final List<T> content, final boolean hasNext) {
        this.content = content;
        this.hasNext = hasNext;
    }

    public List<T> getContent() {
        return this.content;
    }

    public boolean hasNext() {
        return this.hasNext;
    }
}
