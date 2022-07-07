package com.woowacourse.f12.presentation;

import com.woowacourse.f12.exception.InvalidPageSizeException;
import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CustomPageableHandlerMethodArgumentResolver extends PageableHandlerMethodArgumentResolver {

    private static final int MAX_SIZE = 150;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return super.supportsParameter(parameter);
    }

    @Override
    public Pageable resolveArgument(final MethodParameter methodParameter, final ModelAndViewContainer mavContainer,
                                    final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        final String sizeArgument = webRequest.getParameter("size");
        validateSize(sizeArgument);
        return super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
    }

    private void validateSize(final String sizeArgument) {
        if (Objects.nonNull(sizeArgument) && Integer.parseInt(sizeArgument) > MAX_SIZE) {
            throw new InvalidPageSizeException(MAX_SIZE);
        }
    }
}
