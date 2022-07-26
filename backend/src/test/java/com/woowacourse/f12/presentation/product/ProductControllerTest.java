package com.woowacourse.f12.presentation.product;

import static com.woowacourse.f12.support.ProductFixture.KEYBOARD_1;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.f12.application.auth.JwtProvider;
import com.woowacourse.f12.application.product.ProductService;
import com.woowacourse.f12.dto.response.product.ProductPageResponse;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import com.woowacourse.f12.exception.notfound.KeyboardNotFoundException;
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
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void 키보드_목록_페이지_조회_성공() throws Exception {
        // given
        given(productService.findPage(any(Pageable.class)))
                .willReturn(ProductPageResponse.from(new SliceImpl<>(List.of(KEYBOARD_1.생성(1L)))));

        // when
        mockMvc.perform(get("/api/v1/products?page=0&size=150&sort=rating,desc"))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(productService).findPage(PageRequest.of(0, 150, Sort.by("rating").descending()));
    }

    @Test
    void 키보드_단일_조회_성공() throws Exception {
        // given
        given(productService.findById(anyLong()))
                .willReturn(ProductResponse.from(KEYBOARD_1.생성(1L)));

        // when
        mockMvc.perform(get("/api/v1/products/" + 1L))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(productService).findById(1L);
    }

    @Test
    void 키보드_단일_조회_실패_존재_하지_않는_아이디() throws Exception {
        // given
        given(productService.findById(anyLong()))
                .willThrow(new KeyboardNotFoundException());

        // when
        mockMvc.perform(get("/api/v1/products/0"))
                .andExpect(status().isNotFound())
                .andDo(print());

        // then
        verify(productService).findById(0L);
    }
}
