package com.woowacourse.f12.presentation;

import com.woowacourse.f12.exception.badrequest.InvalidPageNumberFormatException;
import com.woowacourse.f12.exception.badrequest.InvalidPageSizeException;
import com.woowacourse.f12.exception.badrequest.InvalidPageSizeFormatException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CustomPageableArgumentResolver extends PageableHandlerMethodArgumentResolver {

    private static final int MAX_SIZE = 150;
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]*$");

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return super.supportsParameter(parameter);
    }

    @Override
    public Pageable resolveArgument(final MethodParameter methodParameter, final ModelAndViewContainer mavContainer,
                                    final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        final String pageArgument = webRequest.getParameter("page");
        validatePage(pageArgument);
        final String sizeArgument = webRequest.getParameter("size");
        validateSize(sizeArgument);
        final Pageable pageable = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
        return addSecondarySortById(pageable);
    }

    private Pageable addSecondarySortById(final Pageable pageable) {
        final Sort sort = pageable.getSort().and(Sort.by("id").descending());
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }

    private void validatePage(final String pageArgument) {
        if (Objects.isNull(pageArgument)) {
            return;
        }
        final Matcher matcher = NUMBER_PATTERN.matcher(pageArgument);
        if (!matcher.matches()) {
            throw new InvalidPageNumberFormatException();
        }
    }

    private void validateSize(final String sizeArgument) {
        if (Objects.isNull(sizeArgument)) {
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
