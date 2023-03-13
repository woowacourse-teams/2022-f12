package com.woowacourse.f12.domain.statistics;

import com.woowacourse.f12.domain.member.MemberInfo;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MemberInfoStatistics<E extends Countable, T extends MemberInfo> {

    private static final int DECIMAL_PLACE = 2;
    private static final double DEFAULT_VALUE = 0.00;
    private static final double DECIMAL = 10.0;
    private final List<E> elements;

    public MemberInfoStatistics(final List<E> elements) {
        this.elements = elements;
    }

    public Map<T, Double> calculateStatistics(final T[] values) {
        final long totalCount = calculateTotalCount();
        return Arrays.stream(values)
                .collect(Collectors.toMap(Function.identity(),
                        memberInfo -> calculateProportion(memberInfo, totalCount)));
    }

    private long calculateTotalCount() {
        return elements.stream()
                .mapToLong(Countable::getCount)
                .sum();
    }

    private Double calculateProportion(final T t, final long totalCount) {
        return elements.stream()
                .filter(it -> it.getValue().equals(t))
                .findAny()
                .map(countable -> round(countable.getCount() / (double) totalCount))
                .orElse(DEFAULT_VALUE);
    }

    private double round(double number) {
        final double operand = Math.pow(DECIMAL, DECIMAL_PLACE);
        return Math.round(number * operand) / operand;
    }
}
