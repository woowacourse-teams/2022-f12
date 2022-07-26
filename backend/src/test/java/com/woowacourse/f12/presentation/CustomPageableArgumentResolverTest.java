package com.woowacourse.f12.presentation;

import static com.woowacourse.f12.domain.product.Category.KEYBOARD;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.f12.application.auth.JwtProvider;
import com.woowacourse.f12.application.product.ProductService;
import com.woowacourse.f12.dto.response.product.ProductPageResponse;
import com.woowacourse.f12.presentation.product.ProductController;
import com.woowacourse.f12.support.AuthTokenExtractor;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
@Import({AuthTokenExtractor.class, JwtProvider.class})
public class CustomPageableArgumentResolverTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void 페이징_실패_페이지_번호_숫자_형식_아님() throws Exception {
        // given
        given(productService.findPage(eq(KEYBOARD), any(Pageable.class)))
                .willReturn(ProductPageResponse.from(new SliceImpl<>(List.of())));

        // when
        mockMvc.perform(get("/api/v1/products?category=KEYBOARD&page=abc&size=10&sort=rating,desc"))
                .andExpect(status().isBadRequest())
                .andDo(print());

        // then
        verify(productService, times(0))
                .findPage(eq(KEYBOARD), any(Pageable.class));
    }

    @Test
    void 페이징_실패_최대_페이징_크기_초과() throws Exception {
        // given
        given(productService.findPage(eq(KEYBOARD), any(Pageable.class)))
                .willReturn(ProductPageResponse.from(new SliceImpl<>(List.of())));

        // when
        mockMvc.perform(get("/api/v1/products?category=KEYBOARD&page=0&size=151&sort=rating,desc"))
                .andExpect(status().isBadRequest())
                .andDo(print());

        // then
        verify(productService, times(0))
                .findPage(KEYBOARD, PageRequest.of(0, 151, Sort.by("rating").descending()));
    }

    @Test
    void 페이징_실패_페이징_크기_숫자_형식_아님() throws Exception {
        // given
        given(productService.findPage(eq(KEYBOARD), any(Pageable.class)))
                .willReturn(ProductPageResponse.from(new SliceImpl<>(List.of())));

        // when
        mockMvc.perform(get("/api/v1/products?category=KEYBOARD&page=0&size=abc&sort=rating,desc"))
                .andExpect(status().isBadRequest())
                .andDo(print());

        // then
        verify(productService, times(0))
                .findPage(eq(KEYBOARD), any(Pageable.class));
    }

    @Test
    void 페이징_성공_페이징_값_지정_안할_경우_기본값으로_페이징() throws Exception {
        // given
        given(productService.findPage(eq(KEYBOARD), any(Pageable.class)))
                .willReturn(ProductPageResponse.from(new SliceImpl<>(List.of())));

        // when
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(productService)
                .findPage(null, PageRequest.of(0, 20));
    }
}
