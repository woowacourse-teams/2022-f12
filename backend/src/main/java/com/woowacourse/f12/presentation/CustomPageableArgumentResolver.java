package com.woowacourse.f12.presentation;

import com.woowacourse.f12.exception.badrequest.InvalidPageNumberFormatException;
import com.woowacourse.f12.exception.badrequest.InvalidPageSizeException;
import com.woowacourse.f12.exception.badrequest.InvalidPageSizeFormatException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CustomPageableArgumentResolver extends PageableHandlerMethodArgumentResolver {

    private static final int MAX_SIZE = 150;
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]*$");

    @Override
    public boolean supportsParameter(@Nonnull final MethodParameter parameter) {
        return super.supportsParameter(parameter);
    }

    @Override
    @Nonnull
    public Pageable resolveArgument(@Nonnull final MethodParameter methodParameter,
                                    final ModelAndViewContainer mavContainer,
                                    final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        final String pageArgument = webRequest.getParameter("page");
        validatePage(pageArgument);
        final String sizeArgument = webRequest.getParameter("size");
        validateSize(sizeArgument);
        final Pageable pageable = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
        final Sort completedSort = addAdditionalSort(webRequest, pageable.getSort());
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), completedSort);
    }

    private Sort addAdditionalSort(final NativeWebRequest webRequest, Sort sort) {
        final String requestURL = ((ServletWebRequest) webRequest).getRequest().getRequestURI();
        if (requestURL.equals("/api/v1/products")) {
            sort = addSecondarySortByProductSort(sort);
        }
        return addFinallySortById(sort);
    }

    private Sort addSecondarySortByProductSort(final Sort sort) {
        final Order ratingOrder = sort.getOrderFor("rating");
        final Order reviewCountOrder = sort.getOrderFor("reviewCount");
        if (ratingOrder != null) {
            return sort.and(Sort.by("reviewCount").descending());
        }
        if (reviewCountOrder != null) {
            return sort.and(Sort.by("rating")).descending();
        }
        return sort;
    }

    private Sort addFinallySortById(final Sort sort) {
        return sort.and(Sort.by("id").descending());
    }

    private void validatePage(final String pageArgument) {
        if (pageArgument == null) {
            return;
        }
        final Matcher matcher = NUMBER_PATTERN.matcher(pageArgument);
        if (!matcher.matches()) {
            throw new InvalidPageNumberFormatException();
        }
    }

    private void validateSize(final String sizeArgument) {
        if (sizeArgument == null) {
            return;
        }
        final Matcher matcher = NUMBER_PATTERN.matcher(sizeArgument);
        if (!matcher.matches()) {
            throw new InvalidPageSizeFormatException();
        }
        if (Integer.parseInt(sizeArgument) > MAX_SIZE) {
            throw new InvalidPageSizeException(MAX_SIZE);
        }
    }
}
