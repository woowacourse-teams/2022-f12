package com.woowacourse.f12.acceptance;

import static com.woowacourse.f12.acceptance.support.LoginUtil.로그인을_한다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_PATCH_요청을_보낸다;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.SENIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.BACKEND_CONSTANT;
import static com.woowacourse.f12.support.GitHubProfileFixtures.CORINNE_GITHUB;
import static com.woowacourse.f12.support.GitHubProfileFixtures.MINCHO_GITHUB;
import static com.woowacourse.f12.support.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.InventoryProductFixtures.UNSELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.ProductFixture.KEYBOARD_1;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_5;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProductRepository;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.domain.product.ProductRepository;
import com.woowacourse.f12.dto.request.inventoryproduct.ProfileProductRequest;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.response.auth.LoginMemberResponse;
import com.woowacourse.f12.dto.response.auth.LoginResponse;
import com.woowacourse.f12.dto.response.inventoryproduct.InventoryProductResponse;
import com.woowacourse.f12.dto.response.inventoryproduct.InventoryProductsResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithProductResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

class InventoryProductAcceptanceTest extends AcceptanceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryProductRepository inventoryProductRepository;

    @Test
    void 리뷰를_작성하면_해당_장비가_인벤토리에_추가된다() {
        // given
        Long productId = 제품을_저장한다(KEYBOARD_1.생성()).getId();
        String token = 로그인을_한다(CORINNE_GITHUB.getCode()).getToken();
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);
        REVIEW_RATING_5.작성_요청을_보낸다(productId, token);

        // when
        List<InventoryProductResponse> keyboardsInInventory =
                로그인된_상태로_GET_요청을_보낸다("/api/v1/members/inventoryProducts", token)
                        .as(InventoryProductsResponse.class)
                        .getItems();

        // then
        assertThat(keyboardsInInventory).extracting("id")
                .containsOnly(productId);
    }

    @Test
    void 대표_장비가_없는_상태에서_대표_장비를_등록한다() {
        // given
        Product product = 제품을_저장한다(KEYBOARD_1.생성());
        LoginResponse loginResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        String token = loginResponse.getToken();
        Member member = 응답을_회원으로_변환한다(loginResponse.getMember());

        InventoryProduct inventoryProduct = UNSELECTED_INVENTORY_PRODUCT.생성(member, product);
        InventoryProduct savedInventoryProduct = 인벤토리에_장비를_추가한다(inventoryProduct);

        // when
        ExtractableResponse<Response> profileProductResponse = 로그인된_상태로_PATCH_요청을_보낸다(
                "api/v1/members/inventoryProducts", token,
                new ProfileProductRequest(List.of(savedInventoryProduct.getId())));

        List<InventoryProductResponse> inventoryProductResponses = 로그인된_상태로_GET_요청을_보낸다(
                "/api/v1/members/inventoryProducts",
                token)
                .as(InventoryProductsResponse.class).getItems();

        // then
        assertAll(
                () -> assertThat(profileProductResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(inventoryProductResponses.get(0).isSelected()).isTrue()
        );
    }

    @Test
    void 대표_장비가_있는_상태에서_대표_장비를_모두_해제한다() {
        // given
        Product product = 제품을_저장한다(KEYBOARD_1.생성());
        LoginResponse loginResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        String token = loginResponse.getToken();
        Member member = 응답을_회원으로_변환한다(loginResponse.getMember());

        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(member, product);
        인벤토리에_장비를_추가한다(inventoryProduct);

        // when
        ExtractableResponse<Response> profileProductResponse = 로그인된_상태로_PATCH_요청을_보낸다(
                "api/v1/members/inventoryProducts", token,
                new ProfileProductRequest(List.of()));

        List<InventoryProductResponse> inventoryProductResponses = 로그인된_상태로_GET_요청을_보낸다(
                "/api/v1/members/inventoryProducts",
                token)
                .as(InventoryProductsResponse.class).getItems();

        // then
        assertAll(
                () -> assertThat(profileProductResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(inventoryProductResponses.get(0).isSelected()).isFalse()
        );
    }

    @Test
    void 등록된_장비_목록을_대표_장비를_포함해서_조회한다() {
        // given
        Product product = 제품을_저장한다(KEYBOARD_1.생성());
        LoginResponse loginResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        String token = loginResponse.getToken();
        Member member = 응답을_회원으로_변환한다(loginResponse.getMember());
        InventoryProduct selectedInventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(member, product);
        InventoryProduct savedSelectedInventoryProduct = 인벤토리에_장비를_추가한다(selectedInventoryProduct);
        InventoryProduct unselectedInventoryProduct = UNSELECTED_INVENTORY_PRODUCT.생성(member, product);
        InventoryProduct savedUnselectedInventoryProduct = 인벤토리에_장비를_추가한다(unselectedInventoryProduct);

        // when
        ExtractableResponse<Response> profileProductResponse = 로그인된_상태로_GET_요청을_보낸다(
                "api/v1/members/inventoryProducts", token);

        // then
        assertAll(
                () -> assertThat(profileProductResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(profileProductResponse.as(InventoryProductsResponse.class).getItems())
                        .usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(InventoryProductResponse.from(savedSelectedInventoryProduct),
                                InventoryProductResponse.from(savedUnselectedInventoryProduct))
        );
    }

    @Test
    void 다른_회원의_아이디로_등록된_장비를_조회한다() {
        // given
        LoginResponse loginResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        Member member = 응답을_회원으로_변환한다(loginResponse.getMember());
        Product product = 제품을_저장한다(KEYBOARD_1.생성());
        InventoryProduct selectedInventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(member, product);
        InventoryProduct savedSelectedInventoryProduct = 인벤토리에_장비를_추가한다(selectedInventoryProduct);
        InventoryProduct unselectedInventoryProduct = UNSELECTED_INVENTORY_PRODUCT.생성(member, product);
        InventoryProduct savedUnselectedInventoryProduct = 인벤토리에_장비를_추가한다(unselectedInventoryProduct);

        // when
        ExtractableResponse<Response> profileProductResponse = GET_요청을_보낸다(
                "/api/v1/members/" + member.getId() + "/inventoryProducts");

        // then
        assertAll(
                () -> assertThat(profileProductResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(profileProductResponse.as(InventoryProductsResponse.class).getItems())
                        .usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(InventoryProductResponse.from(savedSelectedInventoryProduct),
                                InventoryProductResponse.from(savedUnselectedInventoryProduct))
        );
    }

    @Test
    void 인벤토리_아이디로_리뷰를_조회한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        Product keyboard = 제품을_저장한다(KEYBOARD_1.생성());

        LoginResponse firstLoginResponse = 로그인을_한다(MINCHO_GITHUB.getCode());
        String token = firstLoginResponse.getToken();
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);

        ExtractableResponse<Response> reviewSaveResponse = REVIEW_RATING_5.작성_요청을_보낸다(keyboard.getId(), token);
        Long reviewId = Long.parseLong(reviewSaveResponse.header("Location").split("/")[4]);

        ExtractableResponse<Response> inventoryProductResponse = 로그인된_상태로_GET_요청을_보낸다(
                "/api/v1/members/inventoryProducts", token);
        Long inventoryProductId = inventoryProductResponse.as(InventoryProductsResponse.class)
                .getItems()
                .get(0)
                .getId();

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/inventoryProducts/" + inventoryProductId + "/reviews");

        // then
        ReviewWithProductResponse reviewResponse = response.as(ReviewWithProductResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(reviewResponse.getId()).isEqualTo(reviewId)
        );
    }

    private Product 제품을_저장한다(Product product) {
        return productRepository.save(product);
    }

    private InventoryProduct 인벤토리에_장비를_추가한다(InventoryProduct inventoryProduct) {
        return inventoryProductRepository.save(inventoryProduct);
    }

    private Member 응답을_회원으로_변환한다(LoginMemberResponse loginMemberResponse) {
        return Member.builder()
                .id(loginMemberResponse.getId())
                .gitHubId(loginMemberResponse.getGitHubId())
                .name(loginMemberResponse.getName())
                .imageUrl(loginMemberResponse.getImageUrl())
                .build();
    }
}
