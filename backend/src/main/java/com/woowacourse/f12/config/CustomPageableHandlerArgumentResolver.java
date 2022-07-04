package com.woowacourse.f12.config;

import com.woowacourse.f12.exception.InvalidPageSizeException;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CustomPageableHandlerArgumentResolver extends PageableHandlerMethodArgumentResolver {

    private static final int MAX_SIZE = 150;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return super.supportsParameter(parameter);
    }

    @Override
    public Pageable resolveArgument(final MethodParameter methodParameter, final ModelAndViewContainer mavContainer,
                                    final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        final int size = Integer.parseInt(webRequest.getParameter("size"));
        if (size > MAX_SIZE) {
            throw new InvalidPageSizeException(MAX_SIZE);
        }
        return super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
    }
}
