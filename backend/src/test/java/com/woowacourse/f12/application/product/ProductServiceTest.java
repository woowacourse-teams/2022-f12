package com.woowacourse.f12.application.product;

import static com.woowacourse.f12.domain.product.Category.KEYBOARD;
import static com.woowacourse.f12.presentation.product.CategoryConstant.KEYBOARD_CONSTANT;
import static com.woowacourse.f12.presentation.product.CategoryConstant.MOUSE_CONSTANT;
import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_1;
import static com.woowacourse.f12.support.fixture.ProductFixture.MOUSE_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.domain.product.ProductRepository;
import com.woowacourse.f12.dto.request.product.ProductCreateRequest;
import com.woowacourse.f12.dto.request.product.ProductSearchRequest;
import com.woowacourse.f12.dto.request.product.ProductUpdateRequest;
import com.woowacourse.f12.dto.response.product.ProductPageResponse;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import com.woowacourse.f12.exception.notfound.ProductNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private PopularProductsCreator popularProductsCreator;

    @InjectMocks
    private ProductService productService;

    @Test
    void 제품_추가_요청으로_제품을_저장한다() {
        // given
        Product savedProduct = KEYBOARD_1.생성(1L);
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("키보드1", "이미지 주소", KEYBOARD_CONSTANT);

        given(productRepository.save(any(Product.class)))
                .willReturn(savedProduct);

        // when
        Long savedId = productService.save(productCreateRequest);

        // then
        assertAll(
                () -> assertThat(savedId).isEqualTo(1L),
                () -> verify(productRepository).save(any(Product.class))
        );
    }

    @Test
    void id_값으로_제품을_조회한다() {
        // given
        Product product = KEYBOARD_1.생성(1L);

        given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(product));

        // when
        ProductResponse productResponse = productService.findById(1L);

        // then
        assertAll(
                () -> verify(productRepository).findById(1L),
                () -> assertThat(productResponse).usingRecursiveComparison()
                        .isEqualTo(ProductResponse.from(product))
        );
    }

    @Test
    void 존재하지_않는_id_값으로_제품을_조회하면_예외를_반환한다() {
        // given
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when then
        assertAll(
                () -> assertThatThrownBy(() -> productService.findById(1L))
                        .isExactlyInstanceOf(ProductNotFoundException.class),
                () -> verify(productRepository).findById(1L)
        );
    }

    @Test
    void 제품명과_카테고리_없이_제품_목록을_조회한다() {
        // given
        Product product = KEYBOARD_1.생성(1L);
        Pageable pageable = PageRequest.of(0, 2);
        ProductSearchRequest productSearchRequest = new ProductSearchRequest(null, null);
        given(productRepository.findWithoutSearchConditions(pageable))
                .willReturn(new SliceImpl<>(List.of(product), pageable, false));

        // when
        ProductPageResponse productPageResponse = productService.findBySearchConditions(productSearchRequest, pageable);

        // then
        assertAll(
                () -> verify(productRepository).findWithoutSearchConditions(pageable),
                () -> assertThat(productPageResponse.isHasNext()).isFalse(),
                () -> assertThat(productPageResponse.getItems()).usingRecursiveFieldByFieldElementComparator()
                        .containsExactly(ProductResponse.from(product))
        );
    }

    @Test
    void 제품명과_카테고리로_제품_목록을_조회한다() {
        // given
        Product product = KEYBOARD_1.생성(1L);
        Pageable pageable = PageRequest.of(0, 2);
        given(productRepository.findWithSearchConditions("키보드1", KEYBOARD, pageable))
                .willReturn(new SliceImpl<>(List.of(product), pageable, false));
        ProductSearchRequest productSearchRequest = new ProductSearchRequest("키보드1", KEYBOARD_CONSTANT);

        // when
        ProductPageResponse productPageResponse = productService.findBySearchConditions(productSearchRequest, pageable);

        // then
        assertAll(
                () -> verify(productRepository).findWithSearchConditions("키보드1", KEYBOARD, pageable),
                () -> assertThat(productPageResponse.isHasNext()).isFalse(),
                () -> assertThat(productPageResponse.getItems()).usingRecursiveFieldByFieldElementComparator()
                        .containsExactly(ProductResponse.from(product))
        );
    }

    @Test
    void 인기_제품을_조회한다() {
        // given
        given(popularProductsCreator.create(anyInt(), any()))
                .willReturn(List.of());

        // when
        productService.findPopularProducts(1);

        // then
        verify(popularProductsCreator).create(anyInt(), any());
    }

    @Test
    void 제품_정보를_수정한다() {
        // given
        Product target = KEYBOARD_1.생성(1L);
        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest("마우스1", "이미지 주소", MOUSE_CONSTANT);

        given(productRepository.findById(1L))
                .willReturn(Optional.of(target));

        // when
        productService.update(1L, productUpdateRequest);

        // then
        assertAll(
                () -> assertThat(target).usingRecursiveComparison()
                        .isEqualTo(MOUSE_1.생성(1L)),
                () -> verify(productRepository).findById(1L)
        );
    }

    @Test
    void 없는_제품을_수정하려고_하면_예외를_반환한다() {
        // given
        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest("마우스1", "이미지 주소", MOUSE_CONSTANT);

        given(productRepository.findById(1L))
                .willReturn(Optional.empty());

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> productService.update(1L, productUpdateRequest)),
                () -> verify(productRepository).findById(1L)
        );
    }

    @Test
    void 제품을_삭제한다() {
        // given
        Product target = KEYBOARD_1.생성(1L);

        given(productRepository.findById(1L))
                .willReturn(Optional.of(target));

        // when
        productService.delete(1L);

        // then
        assertAll(
                () -> verify(productRepository).findById(1L),
                () -> verify(productRepository).delete(target),
                () -> verify(eventPublisher).publishEvent(any(ProductDeletedEvent.class))
        );
    }

    @Test
    void 없는_제품을_삭제하려고_하면_예외를_반환한다() {
        // given
        given(productRepository.findById(1L))
                .willReturn(Optional.empty());

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> productService.delete(1L)),
                () -> verify(productRepository).findById(1L),
                () -> verify(productRepository, times(0)).delete(any(Product.class))
        );
    }
}
