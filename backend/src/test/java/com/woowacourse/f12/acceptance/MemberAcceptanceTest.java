package com.woowacourse.f12.acceptance;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.domain.product.ProductRepository;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.response.auth.LoginMemberResponse;
import com.woowacourse.f12.dto.response.auth.LoginResponse;
import com.woowacourse.f12.dto.response.member.MemberPageResponse;
import com.woowacourse.f12.dto.response.member.MemberResponse;
import com.woowacourse.f12.dto.response.member.MemberWithProfileProductResponse;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.woowacourse.f12.acceptance.support.LoginUtil.로그인을_한다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.*;
import static com.woowacourse.f12.domain.member.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.member.CareerLevel.SENIOR;
import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.JUNIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.SENIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.BACKEND_CONSTANT;
import static com.woowacourse.f12.support.GitHubProfileFixtures.*;
import static com.woowacourse.f12.support.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.InventoryProductFixtures.대표_장비_업데이트_한다;
import static com.woowacourse.f12.support.MemberFixtures.CORINNE;
import static com.woowacourse.f12.support.MemberFixtures.MINCHO;
import static com.woowacourse.f12.support.ProductFixture.KEYBOARD_1;
import static com.woowacourse.f12.support.ProductFixture.MOUSE_1;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_4;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_5;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MemberAcceptanceTest extends AcceptanceTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 로그인_된_상태에서_내_회원정보를_업데이트한다() {
        // given
        LoginResponse loginResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        String token = loginResponse.getToken();
        MemberRequest memberRequest = new MemberRequest(JUNIOR_CONSTANT, BACKEND_CONSTANT);

        // when
        ExtractableResponse<Response> memberUpdatedResponse = 로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token,
                memberRequest);
        ExtractableResponse<Response> memberGetResponse = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/me", token);

        // then
        Member member = Member.builder()
                .careerLevel(JUNIOR)
                .jobType(BACKEND)
                .build();
        assertAll(
                () -> assertThat(memberGetResponse.as(MemberResponse.class)).usingRecursiveComparison()
                        .comparingOnlyFields("careerLevel", "jobType")
                        .isEqualTo(MemberResponse.from(member, false)),
                () -> assertThat(memberUpdatedResponse.statusCode()).isEqualTo(HttpStatus.OK.value())
        );
    }

    @Test
    void 로그인_된_상태에서_내_회원정보를_조회한다() {
        // given
        LoginResponse loginResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        String token = loginResponse.getToken();
        LoginMemberResponse loginMemberResponse = loginResponse.getMember();

        MemberRequest memberRequest = new MemberRequest(JUNIOR_CONSTANT, BACKEND_CONSTANT);
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);
        Member expectedMember = CORINNE.추가정보를_입력하여_생성(loginMemberResponse.getId(), JUNIOR, BACKEND);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/me", token);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(MemberResponse.class)).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.from(expectedMember, false))
        );
    }

    @Test
    void 로그인_된_상태에서_추가정보가_입력되지_않았을떄_내_회원정보를_조회한다() {
        // given
        LoginResponse loginResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        String token = loginResponse.getToken();

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/me", token);

        // then
        Member expectedMember = 응답을_회원으로_변환한다(loginResponse.getMember());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(MemberResponse.class)).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.from(expectedMember, false))
        );
    }

    @Test
    void 비로그인_상태에서_회원정보를_조회한다() {
        // given
        LoginResponse loginResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        String token = loginResponse.getToken();
        LoginMemberResponse loginMemberResponse = loginResponse.getMember();

        MemberRequest memberRequest = new MemberRequest(JUNIOR_CONSTANT, BACKEND_CONSTANT);
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/members/" + loginMemberResponse.getId());

        // then
        Member expectedMember = Member.builder()
                .id(loginMemberResponse.getId())
                .name(loginMemberResponse.getName())
                .gitHubId(loginMemberResponse.getGitHubId())
                .imageUrl(loginMemberResponse.getImageUrl())
                .careerLevel(JUNIOR)
                .jobType(BACKEND)
                .build();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(MemberResponse.class)).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.from(expectedMember, false))
        );
    }

    @Test
    void 로그인_상태에서_팔로우한_회원의_정보를_조회한다() {
        // given
        LoginResponse firstLoginResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        String firstToken = firstLoginResponse.getToken();

        MemberRequest memberRequest = new MemberRequest(JUNIOR_CONSTANT, BACKEND_CONSTANT);
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", firstToken, memberRequest);

        LoginResponse secondLoginResponse = 로그인을_한다(MINCHO_GITHUB.getCode());
        String secondToken = secondLoginResponse.getToken();
        LoginMemberResponse secondLoginResponseMember = secondLoginResponse.getMember();
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", secondToken, memberRequest);

        로그인된_상태로_POST_요청을_보낸다("/api/v1/members/" + secondLoginResponseMember.getId() + "/following", firstToken);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/" + secondLoginResponseMember.getId(), firstToken);

        // then
        Member expectedMember = Member.builder()
                .id(secondLoginResponseMember.getId())
                .name(secondLoginResponseMember.getName())
                .gitHubId(secondLoginResponseMember.getGitHubId())
                .imageUrl(secondLoginResponseMember.getImageUrl())
                .careerLevel(JUNIOR)
                .jobType(BACKEND)
                .followerCount(1)
                .build();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(MemberResponse.class)).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.from(expectedMember, true))
        );
    }

    @Test
    void 로그인_상태에서_팔로우하지_않은_회원의_정보를_조회한다() {
        // given
        LoginResponse firstLoginResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        String firstToken = firstLoginResponse.getToken();

        MemberRequest memberRequest = new MemberRequest(JUNIOR_CONSTANT, BACKEND_CONSTANT);
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", firstToken, memberRequest);

        LoginResponse secondLoginResponse = 로그인을_한다(MINCHO_GITHUB.getCode());
        String secondToken = secondLoginResponse.getToken();
        LoginMemberResponse secondLoginResponseMember = secondLoginResponse.getMember();
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", secondToken, memberRequest);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/" + secondLoginResponseMember.getId(), firstToken);

        // then
        Member expectedMember = Member.builder()
                .id(secondLoginResponseMember.getId())
                .name(secondLoginResponseMember.getName())
                .gitHubId(secondLoginResponseMember.getGitHubId())
                .imageUrl(secondLoginResponseMember.getImageUrl())
                .careerLevel(JUNIOR)
                .jobType(BACKEND)
                .followerCount(0)
                .build();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(MemberResponse.class)).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.from(expectedMember, false))
        );
    }

    @Test
    void 비회원이_회원정보를_키워드와_옵션을_입력하지않고_조회한다() {
        // given
        Product product = 제품을_저장한다(KEYBOARD_1.생성());

        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        LoginResponse firstLoginResponse = 로그인을_한다(MINCHO_GITHUB.getCode());
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", firstLoginResponse.getToken(), memberRequest);

        LoginResponse secondLoginResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        String token = secondLoginResponse.getToken();
        Long memberId = secondLoginResponse.getMember().getId();
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);

        REVIEW_RATING_5.작성_요청을_보낸다(product.getId(), token);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/members?page=0&size=2");

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(1L, CORINNE.생성(memberId), product);
        Member member = CORINNE.인벤토리를_추가해서_생성(memberId, inventoryProduct);
        MemberWithProfileProductResponse memberWithProfileProductResponse = MemberWithProfileProductResponse.of(
                member, false);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(
                        memberPageResponse.getItems()).usingRecursiveFieldByFieldElementComparatorIgnoringFields(
                                "profileProducts")
                        .hasSize(2)
                        .contains(memberWithProfileProductResponse)
        );
    }

    @Test
    void 비회원이_회원정보를_키워드와_옵션을_입력하지않고_조회할때_추가정보가_입력되지않은_회원은_포함되지_않는다() {
        // given
        Product product = 제품을_저장한다(KEYBOARD_1.생성());

        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        로그인을_한다(MINCHO_GITHUB.getCode());

        LoginResponse secondLoginResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        String token = secondLoginResponse.getToken();
        Long memberId = secondLoginResponse.getMember().getId();
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);

        REVIEW_RATING_5.작성_요청을_보낸다(product.getId(), token);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/members?page=0&size=2");

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(1L, CORINNE.생성(memberId), product);
        Member member = CORINNE.인벤토리를_추가해서_생성(memberId, inventoryProduct);
        MemberWithProfileProductResponse memberWithProfileProductResponse = MemberWithProfileProductResponse.of(
                member, false);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(
                        memberPageResponse.getItems()).usingRecursiveFieldByFieldElementComparatorIgnoringFields(
                                "profileProducts")
                        .hasSize(1)
                        .contains(memberWithProfileProductResponse)
        );
    }

    @Test
    void 비회원이_회원정보를_옵션으로_검색하여_조회한다() {
        // given
        Product product = 제품을_저장한다(KEYBOARD_1.생성());

        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        LoginResponse firstLoginResponse = 로그인을_한다(MINCHO_GITHUB.getCode());
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", firstLoginResponse.getToken(), memberRequest);

        LoginResponse secondLoginResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        String token = secondLoginResponse.getToken();
        Long memberId = secondLoginResponse.getMember().getId();
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);

        REVIEW_RATING_5.작성_요청을_보낸다(product.getId(), token);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/members?page=0&size=2&careerLevel=senior&jobType=backend");

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(1L, CORINNE.생성(memberId), product);
        Member member = CORINNE.인벤토리를_추가해서_생성(memberId, inventoryProduct);
        MemberWithProfileProductResponse memberWithProfileProductResponse = MemberWithProfileProductResponse.of(
                member, false);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(
                        memberPageResponse.getItems()).usingRecursiveFieldByFieldElementComparatorIgnoringFields(
                                "profileProducts")
                        .hasSize(2)
                        .contains(memberWithProfileProductResponse)
        );
    }

    @Test
    void 비회원이_회원정보를_키워드와_옵션으로_검색하여_대표_장비와_함께_조회한다() {
        // given
        Product keyboard = 제품을_저장한다(KEYBOARD_1.생성());
        Product mouse = 제품을_저장한다(MOUSE_1.생성());

        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        LoginResponse firstLoginResponse = 로그인을_한다(MINCHO_GITHUB.getCode());
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", firstLoginResponse.getToken(), memberRequest);

        LoginResponse secondLoginResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        String token = secondLoginResponse.getToken();
        Long memberId = secondLoginResponse.getMember().getId();
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);

        REVIEW_RATING_5.작성_요청을_보낸다(keyboard.getId(), token);
        REVIEW_RATING_4.작성_요청을_보낸다(mouse.getId(), token);
        대표_장비_업데이트_한다(List.of(keyboard), token);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/members?page=0&size=2&query=cheese&careerLevel=senior&jobType=backend");

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(1L, CORINNE.생성(memberId), keyboard);
        Member member = CORINNE.인벤토리를_추가해서_생성(memberId, inventoryProduct);
        MemberWithProfileProductResponse memberWithProfileProductResponse = MemberWithProfileProductResponse.of(
                member, false);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems())
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("profileProducts")
                        .hasSize(1)
                        .containsOnly(memberWithProfileProductResponse),
                () -> assertThat(memberPageResponse.getItems().get(0).getProfileProducts())
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("reviewCount", "rating")
                        .hasSize(1)
                        .containsOnly(ProductResponse.from(keyboard))
        );
    }

    @Test
    void 회원이_회원정보를_검색하여_대표_장비와_함께_조회한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse firstLoginResponse = 로그인을_한다(OHZZI_GITHUB.getCode());
        String token = firstLoginResponse.getToken();
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);

        LoginResponse secondLoginResponse = 로그인을_한다(MINCHO_GITHUB.getCode());
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", secondLoginResponse.getToken(), memberRequest);

        LoginResponse thirdLoginResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", thirdLoginResponse.getToken(), memberRequest);

        로그인된_상태로_POST_요청을_보낸다("/api/v1/members/" + secondLoginResponse.getMember().getId() + "/following", token);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members?page=0&size=2", token);

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);
        Member mincho = MINCHO.추가정보를_입력하여_생성(secondLoginResponse.getMember().getId(), SENIOR, BACKEND);
        Member corrine = CORINNE.추가정보를_입력하여_생성(thirdLoginResponse.getMember().getId(), SENIOR, BACKEND);
        MemberWithProfileProductResponse minchoResponse = MemberWithProfileProductResponse.of(mincho, true);
        MemberWithProfileProductResponse corinneResponse = MemberWithProfileProductResponse.of(corrine, false);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isTrue(),
                () -> assertThat(memberPageResponse.getItems())
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("profileProducts", "followerCount")
                        .hasSize(2)
                        .containsExactly(corinneResponse, minchoResponse)
        );
    }

    @Test
    void 회원정보_목록은_id의_역순으로_조회된다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse firstLoginResponse = 로그인을_한다(MINCHO_GITHUB.getCode());
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", firstLoginResponse.getToken(), memberRequest);

        LoginResponse secondLoginResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", secondLoginResponse.getToken(), memberRequest);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/members?page=0&size=2");

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems()
                        .stream()
                        .map(MemberWithProfileProductResponse::getId))
                        .usingRecursiveFieldByFieldElementComparatorOnFields("id")
                        .hasSize(2)
                        .containsExactly(secondLoginResponse.getMember().getId(), firstLoginResponse.getMember().getId())
        );
    }

    @Test
    void 다른_회원을_팔로우한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse firstLoginResponse = 로그인을_한다(MINCHO_GITHUB.getCode());
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", firstLoginResponse.getToken(), memberRequest);

        LoginResponse secondLoginResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", secondLoginResponse.getToken(), memberRequest);

        Long targetId = secondLoginResponse.getMember().getId();

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_POST_요청을_보낸다("/api/v1/members/" + targetId + "/following", firstLoginResponse.getToken());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 팔로우한_회원을_언팔로우한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse firstLoginResponse = 로그인을_한다(MINCHO_GITHUB.getCode());
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", firstLoginResponse.getToken(), memberRequest);

        LoginResponse secondLoginResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", secondLoginResponse.getToken(), memberRequest);

        Long targetId = secondLoginResponse.getMember().getId();
        로그인된_상태로_POST_요청을_보낸다("/api/v1/members/" + targetId + "/following", firstLoginResponse.getToken());

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_DELETE_요청을_보낸다("/api/v1/members/" + targetId + "/following", firstLoginResponse.getToken());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 팔로우하는_회원의_목록을_조회한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse followerLoginResponse = 로그인을_한다(OHZZI_GITHUB.getCode());
        String token = followerLoginResponse.getToken();
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);

        LoginResponse followeeLoginResponse = 로그인을_한다(MINCHO_GITHUB.getCode());
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", followeeLoginResponse.getToken(), memberRequest);

        로그인된_상태로_POST_요청을_보낸다("/api/v1/members/" + followeeLoginResponse.getMember().getId() + "/following", token);

        LoginResponse notFolloweeLoginResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", notFolloweeLoginResponse.getToken(), memberRequest);

        Member mincho = MINCHO.추가정보를_입력하여_생성(followeeLoginResponse.getMember().getId(), SENIOR, BACKEND);
        MemberWithProfileProductResponse followeeResponse = MemberWithProfileProductResponse.of(mincho, true);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/me/followees?page=0&size=1", token);

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems())
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("profileProducts", "followerCount")
                        .hasSize(1)
                        .containsExactly(followeeResponse)
        );
    }

    @Test
    void 팔로우하는_회원의_목록을_검색하여_조회한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse followerLoginResponse = 로그인을_한다(OHZZI_GITHUB.getCode());
        String token = followerLoginResponse.getToken();
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);

        LoginResponse searchedFolloweeResponse = 로그인을_한다(MINCHO_GITHUB.getCode());
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", searchedFolloweeResponse.getToken(), memberRequest);
        로그인된_상태로_POST_요청을_보낸다("/api/v1/members/" + searchedFolloweeResponse.getMember().getId() + "/following", token);

        LoginResponse notSearchedFolloweeResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", notSearchedFolloweeResponse.getToken(), memberRequest);
        로그인된_상태로_POST_요청을_보낸다("/api/v1/members/" + notSearchedFolloweeResponse.getMember().getId() + "/following", token);

        Member mincho = MINCHO.추가정보를_입력하여_생성(searchedFolloweeResponse.getMember().getId(), SENIOR, BACKEND);
        MemberWithProfileProductResponse followeeResponse = MemberWithProfileProductResponse.of(mincho, true);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/me/followees?page=0&size=1&query=js&careerLevel=senior&jobType=backend", token);

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems())
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("profileProducts", "followerCount")
                        .hasSize(1)
                        .containsExactly(followeeResponse)
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
}
