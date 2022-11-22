package com.woowacourse.f12.support;

import java.util.Objects;
import org.springframework.data.domain.Sort;

public class CursorPageable {
    private final Long cursor;
    private final Integer size;
    private final Sort sort;

    public CursorPageable(final Long cursor, final Integer size, final Sort sort) {
        this.cursor = cursor;
        this.size = size;
        this.sort = sort;
    }

    public Long getCursor() {
        return cursor;
    }

    public Integer getSize() {
        return size;
    }

    public Sort getSort() {
        return sort;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CursorPageable that = (CursorPageable) o;
        return Objects.equals(cursor, that.cursor) && Objects.equals(size, that.size)
                && Objects.equals(sort, that.sort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cursor, size, sort);
    }
}
