package com.woowacourse.f12.acceptance;

import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_PATCH_요청을_보낸다;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.SENIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.BACKEND_CONSTANT;
import static com.woowacourse.f12.support.fixture.AcceptanceFixture.CORINNE;
import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_1;
import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_2;
import static com.woowacourse.f12.support.fixture.ReviewFixture.REVIEW_RATING_5;
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
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        String token = CORINNE.로그인을_한다().getToken();
        CORINNE.로그인한_상태로(token).추가정보를_입력한다(memberRequest);
        CORINNE.로그인한_상태로(token).리뷰를_작성한다(productId, REVIEW_RATING_5);

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
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        LoginResponse loginResponse = CORINNE.로그인을_한다();
        String token = loginResponse.getToken();
        CORINNE.로그인한_상태로(token).추가정보를_입력한다(memberRequest);
        CORINNE.로그인한_상태로(token).리뷰를_작성한다(product.getId(), REVIEW_RATING_5);

        Member member = 응답을_회원으로_변환한다(loginResponse.getMember());
        InventoryProduct savedInventoryProduct = 인벤토리에_있는_장비를_찾아온다(product, member);

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
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        LoginResponse loginResponse = CORINNE.로그인을_한다();
        String token = loginResponse.getToken();
        CORINNE.로그인한_상태로(token).추가정보를_입력한다(memberRequest);
        CORINNE.로그인한_상태로(token).리뷰를_작성한다(product.getId(), REVIEW_RATING_5);

        Member member = 응답을_회원으로_변환한다(loginResponse.getMember());
        InventoryProduct savedInventoryProduct = 인벤토리에_있는_장비를_찾아온다(product, member);
        CORINNE.로그인한_상태로(token).대표장비를_등록한다(List.of(savedInventoryProduct.getId()));

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
        Product product1 = 제품을_저장한다(KEYBOARD_1.생성());
        Product product2 = 제품을_저장한다(KEYBOARD_2.생성());
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        LoginResponse loginResponse = CORINNE.로그인을_한다();
        String token = loginResponse.getToken();
        CORINNE.로그인한_상태로(token).추가정보를_입력한다(memberRequest);
        CORINNE.로그인한_상태로(token).리뷰를_작성한다(product1.getId(), REVIEW_RATING_5);
        CORINNE.로그인한_상태로(token).리뷰를_작성한다(product2.getId(), REVIEW_RATING_5);

        Member member = 응답을_회원으로_변환한다(loginResponse.getMember());
        InventoryProduct selectedInventoryProduct = 인벤토리에_있는_장비를_찾아온다(product1, member);
        InventoryProduct unselectedInventoryProduct = 인벤토리에_있는_장비를_찾아온다(product2, member);
        CORINNE.로그인한_상태로(token).대표장비를_등록한다(List.of(selectedInventoryProduct.getId()));
        InventoryProduct profileInventoryProduct = 인벤토리에_있는_장비를_찾아온다(product1, member);

        // when
        ExtractableResponse<Response> profileProductResponse = 로그인된_상태로_GET_요청을_보낸다(
                "api/v1/members/inventoryProducts", token);

        // then
        assertAll(
                () -> assertThat(profileProductResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(profileProductResponse.as(InventoryProductsResponse.class).getItems())
                        .usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(InventoryProductResponse.from(profileInventoryProduct),
                                InventoryProductResponse.from(unselectedInventoryProduct))
        );
    }

    @Test
    void 다른_회원의_아이디로_등록된_장비를_조회한다() {
        // given
        Product product1 = 제품을_저장한다(KEYBOARD_1.생성());
        Product product2 = 제품을_저장한다(KEYBOARD_2.생성());
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        LoginResponse loginResponse = CORINNE.로그인을_한다();
        String token = loginResponse.getToken();
        CORINNE.로그인한_상태로(token).추가정보를_입력한다(memberRequest);
        CORINNE.로그인한_상태로(token).리뷰를_작성한다(product1.getId(), REVIEW_RATING_5);
        CORINNE.로그인한_상태로(token).리뷰를_작성한다(product2.getId(), REVIEW_RATING_5);

        Member member = 응답을_회원으로_변환한다(loginResponse.getMember());
        InventoryProduct selectedInventoryProduct = 인벤토리에_있는_장비를_찾아온다(product1, member);
        InventoryProduct unselectedInventoryProduct = 인벤토리에_있는_장비를_찾아온다(product2, member);
        CORINNE.로그인한_상태로(token).대표장비를_등록한다(List.of(selectedInventoryProduct.getId()));
        InventoryProduct profileInventoryProduct = 인벤토리에_있는_장비를_찾아온다(product1, member);

        // when
        ExtractableResponse<Response> profileProductResponse = GET_요청을_보낸다(
                "/api/v1/members/" + member.getId() + "/inventoryProducts");

        // then
        assertAll(
                () -> assertThat(profileProductResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(profileProductResponse.as(InventoryProductsResponse.class).getItems())
                        .usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(InventoryProductResponse.from(profileInventoryProduct),
                                InventoryProductResponse.from(unselectedInventoryProduct))
        );
    }

    private Product 제품을_저장한다(Product product) {
        return productRepository.save(product);
    }

    private Member 응답을_회원으로_변환한다(LoginMemberResponse loginMemberResponse) {
        return Member.builder()
                .id(loginMemberResponse.getId())
                .gitHubId(loginMemberResponse.getGitHubId())
                .name(loginMemberResponse.getName())
                .imageUrl(loginMemberResponse.getImageUrl())
                .build();
    }

    private InventoryProduct 인벤토리에_있는_장비를_찾아온다(final Product product, final Member member) {
        return inventoryProductRepository.findByMemberAndProduct(member, product).get();
    }
}
