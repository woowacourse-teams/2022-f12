package com.woowacourse.f12.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.f12.exception.ErrorCode;
import org.junit.jupiter.api.Test;

class UrlLengthCheckInterceptorTest extends PresentationTest {

    @Test
    void uri가_1000자_이상인_경우_예외가_발생() throws Exception {
        // given
        final String uriPrefix = "/api/v1/products?page=0&query=";
        final String uriSuffix = "&sort=reviewCount,desc&size=12";
        final String tooLongQuery = "1".repeat(1000 - uriPrefix.length() - uriSuffix.length());

        // when, then
        mockMvc.perform(
                        get(uriPrefix + tooLongQuery + uriSuffix))
                .andExpect(status().isUriTooLong())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.URI_TOO_LONG.getValue()))
                .andDo(print());

        // then
        verify(productService, times(0)).findBySearchConditions(any(), any());
    }
}
