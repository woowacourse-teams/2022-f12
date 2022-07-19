package com.woowacourse.f12.acceptance;

import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_PATCH_요청을_보낸다;
import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.domain.InventoryProduct;
import com.woowacourse.f12.domain.InventoryProductRepository;
import com.woowacourse.f12.domain.Keyboard;
import com.woowacourse.f12.domain.KeyboardRepository;
import com.woowacourse.f12.dto.request.ProfileProductRequest;
import com.woowacourse.f12.dto.response.InventoryProductResponse;
import com.woowacourse.f12.dto.response.InventoryProductsResponse;
import com.woowacourse.f12.dto.response.LoginResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

class InventoryProductAcceptanceTest extends AcceptanceTest {

    @Autowired
    private KeyboardRepository keyboardRepository;

    @Autowired
    private InventoryProductRepository inventoryProductRepository;

    @Test
    void 대표_장비가_없는_상태에서_대표_장비를_등록한다() {
        // given
        Keyboard keyboard = 키보드를_저장한다(KEYBOARD_1.생성());
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/login?code=dkasjbdkjas");
        String token = response.as(LoginResponse.class).getToken();
        Long memberId = response.as(LoginResponse.class).getMember().getId();

        InventoryProduct inventoryProduct = InventoryProduct.builder()
                .memberId(memberId)
                .keyboard(keyboard)
                .build();
        InventoryProduct savedInventoryProduct = inventoryProductRepository.save(inventoryProduct);

        // when
        ExtractableResponse<Response> saveProfileProductResponse = 로그인된_상태로_PATCH_요청을_보낸다(
                "api/v1/members/inventoryProducts", token,
                new ProfileProductRequest(savedInventoryProduct.getId(), null));

        // then
        assertThat(saveProfileProductResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 등록된_장비_목록을_대표_장비를_포함해서_조회한다() {
        // given
        Keyboard keyboard = 키보드를_저장한다(KEYBOARD_1.생성());
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/login?code=dkasjbdkjas");
        String token = response.as(LoginResponse.class).getToken();
        Long memberId = response.as(LoginResponse.class).getMember().getId();
        InventoryProduct selectedInventoryProduct = InventoryProduct.builder()
                .memberId(memberId)
                .keyboard(keyboard)
                .isSelected(true)
                .build();
        InventoryProduct unselectedInventoryProduct = InventoryProduct.builder()
                .memberId(memberId)
                .keyboard(keyboard)
                .isSelected(false)
                .build();
        InventoryProduct savedSelectedInventoryProduct = inventoryProductRepository.save(selectedInventoryProduct);
        InventoryProduct savedUnselectedInventoryProduct = inventoryProductRepository.save(unselectedInventoryProduct);

        // when
        ExtractableResponse<Response> saveProfileProductResponse = 로그인된_상태로_GET_요청을_보낸다(
                "api/v1/members/inventoryProducts", token);

        // then
        assertAll(
                () -> assertThat(saveProfileProductResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(saveProfileProductResponse.as(InventoryProductsResponse.class).getKeyboards())
                        .usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(InventoryProductResponse.from(savedSelectedInventoryProduct),
                                InventoryProductResponse.from(savedUnselectedInventoryProduct))
        );
    }

    @Test
    void 다른_회원의_아이디로_등록된_장비를_조회한다() {
        // given
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/login?code=dkasjbdkjas");
        Long memberId = response.as(LoginResponse.class).getMember().getId();
        Keyboard keyboard = 키보드를_저장한다(KEYBOARD_1.생성());
        InventoryProduct selectedInventoryProduct = InventoryProduct.builder()
                .memberId(memberId)
                .keyboard(keyboard)
                .isSelected(true)
                .build();
        InventoryProduct unselectedInventoryProduct = InventoryProduct.builder()
                .memberId(memberId)
                .keyboard(keyboard)
                .isSelected(false)
                .build();
        InventoryProduct savedSelectedInventoryProduct = inventoryProductRepository.save(selectedInventoryProduct);
        InventoryProduct savedUnselectedInventoryProduct = inventoryProductRepository.save(unselectedInventoryProduct);

        // when
        ExtractableResponse<Response> saveProfileProductResponse = GET_요청을_보낸다(
                "/api/v1/members/" + memberId + "/inventoryProducts");

        // then
        assertAll(
                () -> assertThat(saveProfileProductResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(saveProfileProductResponse.as(InventoryProductsResponse.class).getKeyboards())
                        .usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(InventoryProductResponse.from(savedSelectedInventoryProduct),
                                InventoryProductResponse.from(savedUnselectedInventoryProduct))
        );
    }

    private Keyboard 키보드를_저장한다(Keyboard keyboard) {
        return keyboardRepository.save(keyboard);
    }
}
