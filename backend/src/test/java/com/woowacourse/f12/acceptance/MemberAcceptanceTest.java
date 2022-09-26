package com.woowacourse.f12.acceptance;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.domain.product.ProductRepository;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.response.auth.LoginMemberResponse;
import com.woowacourse.f12.dto.response.auth.LoginResponse;
import com.woowacourse.f12.dto.response.inventoryproduct.InventoryProductResponse;
import com.woowacourse.f12.dto.response.inventoryproduct.InventoryProductsResponse;
import com.woowacourse.f12.dto.response.member.LoggedInMemberResponse;
import com.woowacourse.f12.dto.response.member.MemberPageResponse;
import com.woowacourse.f12.dto.response.member.MemberResponse;
import com.woowacourse.f12.dto.response.member.MemberWithProfileProductResponse;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import com.woowacourse.f12.support.fixture.ProductFixture;
import com.woowacourse.f12.support.fixture.ReviewFixture;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.*;
import static com.woowacourse.f12.domain.member.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.member.CareerLevel.SENIOR;
import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.JUNIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.SENIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.BACKEND_CONSTANT;
import static com.woowacourse.f12.support.fixture.AcceptanceFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MemberAcceptanceTest extends AcceptanceTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 로그인_하고_내_회원정보를_업데이트한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(JUNIOR_CONSTANT, BACKEND_CONSTANT);
        LoginResponse loginResponse = 코린.로그인을_한다();
        String loginToken = loginResponse.getToken();

        // when
        ExtractableResponse<Response> memberUpdatedResponse = 로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", loginToken,
                memberRequest);

        // then
        ExtractableResponse<Response> memberGetResponse = 코린.로그인한_상태로(loginToken).자신의_프로필을_조회한다();
        Member expectedMember = 코린.엔티티를().추가정보를_입력하여_생성(loginResponse.getMember().getId(), JUNIOR, BACKEND);

        assertAll(
                () -> assertThat(memberGetResponse.as(MemberResponse.class)).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.of(expectedMember, false)),
                () -> assertThat(memberUpdatedResponse.statusCode()).isEqualTo(HttpStatus.OK.value())
        );
    }

    @Test
    void 로그인_하고_내_회원정보를_조회한다() {
        // given
        LoginResponse loginResponse = 코린.로그인을_한다();
        String loginToken = loginResponse.getToken();

        MemberRequest memberRequest = new MemberRequest(JUNIOR_CONSTANT, BACKEND_CONSTANT);
        코린.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/me", loginToken);

        // then
        Long loggedInMemberId = loginResponse.getMember().getId();
        Member expectedMember = 코린.엔티티를().추가정보를_입력하여_생성(loggedInMemberId, JUNIOR, BACKEND);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(MemberResponse.class)).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.of(expectedMember, false))
        );
    }

    @Test
    void 로그인_하고_추가정보가_입력되지_않고_내_회원정보를_조회한다() {
        // given
        LoginResponse loginResponse = 코린.로그인을_한다();

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/me", loginResponse.getToken());

        // then
        Member expectedMember = 코린.엔티티를().추가정보_없이_생성(loginResponse.getMember().getId());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(MemberResponse.class)).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.of(expectedMember, false))
        );
    }

    @Test
    void 로그인_하지_않고_회원정보를_조회한다() {
        // given
        LoginResponse loginResponse = 코린.로그인을_한다();

        String loginToken = loginResponse.getToken();
        LoginMemberResponse loginMemberResponse = loginResponse.getMember();
        MemberRequest memberRequest = new MemberRequest(JUNIOR_CONSTANT, BACKEND_CONSTANT);
        코린.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

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
                        .isEqualTo(MemberResponse.of(expectedMember, false))
        );
    }

    @Test
    void 로그인_하고_팔로우한_회원의_정보를_조회한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(JUNIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse firstLoginResponse = 민초.로그인을_한다();
        Long targetId = firstLoginResponse.getMember().getId();
        민초.로그인한_상태로(firstLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse secondLoginResponse = 코린.로그인을_한다();
        String loginToken = secondLoginResponse.getToken();
        코린.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);
        코린.로그인한_상태로(loginToken).팔로우한다(targetId);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/" + targetId, loginToken);

        // then
        LoginMemberResponse firstLoginResponseMember = firstLoginResponse.getMember();
        Member expectedMember = Member.builder()
                .id(firstLoginResponseMember.getId())
                .name(firstLoginResponseMember.getName())
                .gitHubId(firstLoginResponseMember.getGitHubId())
                .imageUrl(firstLoginResponseMember.getImageUrl())
                .careerLevel(JUNIOR)
                .jobType(BACKEND)
                .followerCount(1)
                .build();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(MemberResponse.class)).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.of(expectedMember, true))
        );
    }

    @Test
    void 로그인_하고_팔로우하지_않은_회원의_정보를_조회한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(JUNIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse firstLoginResponse = 민초.로그인을_한다();
        Long targetId = firstLoginResponse.getMember().getId();
        민초.로그인한_상태로(firstLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse secondLoginResponse = 코린.로그인을_한다();
        String loginToken = secondLoginResponse.getToken();
        코린.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/" + targetId, loginToken);

        // then
        LoginMemberResponse firstLoginResponseMember = firstLoginResponse.getMember();
        Member expectedMember = Member.builder()
                .id(firstLoginResponseMember.getId())
                .name(firstLoginResponseMember.getName())
                .gitHubId(firstLoginResponseMember.getGitHubId())
                .imageUrl(firstLoginResponseMember.getImageUrl())
                .careerLevel(JUNIOR)
                .jobType(BACKEND)
                .followerCount(0)
                .build();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(MemberResponse.class)).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.of(expectedMember, false))
        );
    }

    @Test
    void 로그인_하지_않고_회원목록을_키워드와_옵션을_입력하지않고_조회한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse firstLoginResponse = 민초.로그인을_한다();
        민초.로그인한_상태로(firstLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse secondLoginResponse = 코린.로그인을_한다();
        코린.로그인한_상태로(secondLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/members?page=0&size=2&sort=followerCount,desc");

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);

        Member member1 = 민초.엔티티를().추가정보를_입력하여_생성(firstLoginResponse.getMember().getId(), SENIOR, BACKEND);
        Member member2 = 코린.엔티티를().추가정보를_입력하여_생성(secondLoginResponse.getMember().getId(), SENIOR, BACKEND);

        MemberWithProfileProductResponse expectedMemberResponse1 =
                MemberWithProfileProductResponse.of(member1, false);
        MemberWithProfileProductResponse expectedMemberResponse2 =
                MemberWithProfileProductResponse.of(member2, false);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems())
                        .usingRecursiveFieldByFieldElementComparator()
                        .hasSize(2)
                        .containsExactly(expectedMemberResponse2, expectedMemberResponse1)
        );
    }

    @Test
    void 로그인_하지_않고_회원목록을_키워드와_옵션을_입력하지않고_조회할때_추가정보가_입력되지않은_회원은_포함되지_않는다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        민초.로그인을_한다();

        LoginResponse secondLoginResponse = 코린.로그인을_한다();
        코린.로그인한_상태로(secondLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/members?page=0&size=2");

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);

        Member member = 코린.엔티티를().추가정보를_입력하여_생성(secondLoginResponse.getMember().getId(), SENIOR, BACKEND);
        MemberWithProfileProductResponse expectedMemberResponse = MemberWithProfileProductResponse.of(member, false);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems())
                        .usingRecursiveFieldByFieldElementComparator()
                        .hasSize(1)
                        .contains(expectedMemberResponse)
        );
    }

    @Test
    void 로그인_하지_않고_회원목록을_옵션으로_검색하여_조회한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse firstLoginResponse = 민초.로그인을_한다();
        민초.로그인한_상태로(firstLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse secondLoginResponse = 코린.로그인을_한다();
        코린.로그인한_상태로(secondLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/members?page=0&size=2&careerLevel=senior&jobType=backend");

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);

        Member member1 = 민초.엔티티를().추가정보를_입력하여_생성(firstLoginResponse.getMember().getId(), SENIOR, BACKEND);
        Member member2 = 코린.엔티티를().추가정보를_입력하여_생성(secondLoginResponse.getMember().getId(), SENIOR, BACKEND);
        MemberWithProfileProductResponse expectedMemberResponse1 = MemberWithProfileProductResponse.of(member1, false);
        MemberWithProfileProductResponse expectedMemberResponse2 = MemberWithProfileProductResponse.of(member2, false);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems()).usingRecursiveFieldByFieldElementComparator()
                        .hasSize(2)
                        .containsExactly(expectedMemberResponse2, expectedMemberResponse1)
        );
    }

    @Test
    void 로그인_하지_않고_회원목록을_키워드와_옵션으로_검색하여_조회한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        민초.로그인을_하고().추가정보를_입력한다(memberRequest);

        LoginResponse secondLoginResponse = 코린.로그인을_한다();
        코린.로그인한_상태로(secondLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/members?page=0&size=2&query=cheese&careerLevel=senior&jobType=backend");

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);

        Member member = 코린.엔티티를().추가정보를_입력하여_생성(secondLoginResponse.getMember().getId(), SENIOR, BACKEND);
        MemberWithProfileProductResponse expectedMemberResponse = MemberWithProfileProductResponse.of(member, false);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems())
                        .usingRecursiveFieldByFieldElementComparator()
                        .hasSize(1)
                        .containsOnly(expectedMemberResponse)
        );
    }

    @Test
    void 로그인_하지_않고_회원목록을_대표장비를_포함하여_키워드로_조회한다() {
        // given
        Product product = 제품을_저장한다(ProductFixture.KEYBOARD_2.생성());
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        민초.로그인을_하고().추가정보를_입력한다(memberRequest);

        LoginResponse corinneLoginResponse = 코린.로그인을_한다();
        Long corinneId = corinneLoginResponse.getMember().getId();

        코린.로그인한_상태로(corinneLoginResponse.getToken()).추가정보를_입력한다(memberRequest);
        코린.로그인한_상태로(corinneLoginResponse.getToken()).리뷰를_작성한다(product.getId(), ReviewFixture.REVIEW_RATING_2);
        List<InventoryProductResponse> inventoryProducts = 자신의_인벤토리_장비를_조회한다(corinneLoginResponse).getItems();
        InventoryProductResponse inventoryProductResponse = inventoryProducts.get(0);

        코린.로그인한_상태로(corinneLoginResponse.getToken()).대표장비를_등록한다(List.of(inventoryProductResponse.getId()));

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/members?page=0&size=2&query=cheese");

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);

        Member member = 코린.엔티티를().추가정보와_인벤토리를_추가해서_생성(corinneId, SENIOR, BACKEND,
                List.of(인벤토리_엔티티로_변환한다(inventoryProductResponse, corinneId)));
        MemberWithProfileProductResponse expectedMemberResponse = MemberWithProfileProductResponse.of(member, false);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems())
                        .usingRecursiveFieldByFieldElementComparator()
                        .hasSize(1)
                        .containsOnly(expectedMemberResponse)
        );
    }

    @Test
    void 로그인_하고_한명을_팔로우한_뒤_회원목록을_검색한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse firstLoginResponse = 오찌.로그인을_한다();
        오찌.로그인한_상태로(firstLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse secondLoginResponse = 민초.로그인을_한다();
        민초.로그인한_상태로(secondLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse thirdLoginResponse = 코린.로그인을_한다();
        String loginToken = thirdLoginResponse.getToken();
        코린.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

        코린.로그인한_상태로(loginToken).팔로우한다(secondLoginResponse.getMember().getId());

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members?page=0&size=2", loginToken);

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);
        Member member1 = 민초.엔티티를().추가정보와_팔로워_카운트를_입력하여_생성(secondLoginResponse.getMember().getId(), SENIOR, BACKEND, 1);
        Member member2 = 코린.엔티티를().추가정보를_입력하여_생성(thirdLoginResponse.getMember().getId(), SENIOR, BACKEND);
        MemberWithProfileProductResponse expectedMemberResponse1 = MemberWithProfileProductResponse.of(member1, true);
        MemberWithProfileProductResponse expectedMemberResponse2 = MemberWithProfileProductResponse.of(member2, false);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isTrue(),
                () -> assertThat(memberPageResponse.getItems())
                        .usingRecursiveFieldByFieldElementComparator()
                        .hasSize(2)
                        .containsExactly(expectedMemberResponse2, expectedMemberResponse1)
        );
    }

    @Test
    void 회원목록은_id의_역순으로_조회된다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse firstLoginResponse = 민초.로그인을_한다();
        민초.로그인한_상태로(firstLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse secondLoginResponse = 코린.로그인을_한다();
        코린.로그인한_상태로(secondLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

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
                        .containsExactly(secondLoginResponse.getMember().getId(),
                                firstLoginResponse.getMember().getId())
        );
    }

    @Test
    void 로그인_하고_다른_회원을_팔로우한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse firstLoginResponse = 민초.로그인을_한다();
        Long targetId = firstLoginResponse.getMember().getId();
        민초.로그인한_상태로(firstLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse secondLoginResponse = 코린.로그인을_한다();
        String loginToken = secondLoginResponse.getToken();
        코린.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_POST_요청을_보낸다("/api/v1/members/" + targetId + "/following",
                loginToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 로그인_하고_팔로우한_회원을_언팔로우한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse firstLoginResponse = 민초.로그인을_한다();
        Long targetId = firstLoginResponse.getMember().getId();
        민초.로그인한_상태로(firstLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse secondLoginResponse = 코린.로그인을_한다();
        String loginToken = secondLoginResponse.getToken();
        코린.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

        코린.로그인한_상태로(loginToken).팔로우한다(targetId);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_DELETE_요청을_보낸다("/api/v1/members/" + targetId + "/following",
                loginToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 로그인_하고_팔로우하는_회원의_목록을_키워드와_옵션_없이_조회한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse followingLoginResponse = 오찌.로그인을_한다();
        Long followingId = followingLoginResponse.getMember().getId();
        오찌.로그인한_상태로(followingLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse notFollowingLoginResponse = 민초.로그인을_한다();
        민초.로그인한_상태로(notFollowingLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse loginResponse = 코린.로그인을_한다();
        String loginToken = loginResponse.getToken();
        코린.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

        코린.로그인한_상태로(loginToken).팔로우한다(followingId);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/me/followings?page=0&size=1",
                loginToken);

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);

        Member following = 오찌.엔티티를().추가정보와_팔로워_카운트를_입력하여_생성(followingLoginResponse.getMember().getId(), SENIOR, BACKEND, 1);
        MemberWithProfileProductResponse followingResponse = MemberWithProfileProductResponse.of(following, true);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems())
                        .usingRecursiveFieldByFieldElementComparator()
                        .hasSize(1)
                        .containsExactly(followingResponse)
        );
    }

    @Test
    void 로그인_하고_팔로우하는_회원의_목록을_옵션으로만_조회한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse followingLoginResponse = 오찌.로그인을_한다();
        Long followingId = followingLoginResponse.getMember().getId();
        오찌.로그인한_상태로(followingLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse notFollowingLoginResponse = 민초.로그인을_한다();
        민초.로그인한_상태로(notFollowingLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse loginResponse = 코린.로그인을_한다();
        String loginToken = loginResponse.getToken();
        코린.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

        코린.로그인한_상태로(loginToken).팔로우한다(followingId);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다(
                "/api/v1/members/me/followings?page=0&size=1&careerLevel=senior&jobType=backend",
                loginToken);

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);

        Member following = 오찌.엔티티를().추가정보를_입력하여_생성(followingLoginResponse.getMember().getId(), SENIOR, BACKEND);
        MemberWithProfileProductResponse followingResponse = MemberWithProfileProductResponse.of(following, true);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems())
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("followerCount")
                        .hasSize(1)
                        .containsExactly(followingResponse)
        );
    }

    @Test
    void 로그인_하고_팔로우하는_회원의_목록을_키워드와_옵션으로_조회한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse followingLoginResponse = 오찌.로그인을_한다();
        Long followingId = followingLoginResponse.getMember().getId();
        오찌.로그인한_상태로(followingLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse notFollowingLoginResponse = 민초.로그인을_한다();
        민초.로그인한_상태로(notFollowingLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse loginResponse = 코린.로그인을_한다();
        String loginToken = loginResponse.getToken();
        코린.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

        코린.로그인한_상태로(loginToken).팔로우한다(followingId);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다(
                "/api/v1/members/me/followings?page=0&size=1&query=O&careerLevel=senior&jobType=backend", loginToken);

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);

        Member following = 오찌.엔티티를().추가정보와_팔로워_카운트를_입력하여_생성(followingLoginResponse.getMember().getId(), SENIOR, BACKEND, 1);
        MemberWithProfileProductResponse followingResponse = MemberWithProfileProductResponse.of(following, true);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems())
                        .usingRecursiveFieldByFieldElementComparator()
                        .hasSize(1)
                        .containsExactly(followingResponse)
        );
    }

    @Test
    void 로그인_하고_팔로우하는_회원의_목록을_대표장비를_포함하여_검색조건을_설정하여_조회한다() {
        // given
        Product product = 제품을_저장한다(ProductFixture.KEYBOARD_2.생성());
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse followingLoginResponse = 오찌.로그인을_한다();
        Long followingId = followingLoginResponse.getMember().getId();
        오찌.로그인한_상태로(followingLoginResponse.getToken()).추가정보를_입력한다(memberRequest);
        오찌.로그인한_상태로(followingLoginResponse.getToken()).리뷰를_작성한다(product.getId(), ReviewFixture.REVIEW_RATING_2);
        List<InventoryProductResponse> inventoryProducts = 자신의_인벤토리_장비를_조회한다(followingLoginResponse).getItems();
        InventoryProductResponse inventoryProductResponse = inventoryProducts.get(0);
        오찌.로그인한_상태로(followingLoginResponse.getToken()).대표장비를_등록한다(List.of(inventoryProductResponse.getId()));

        LoginResponse notFollowingLoginResponse = 민초.로그인을_한다();
        민초.로그인한_상태로(notFollowingLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse loginResponse = 코린.로그인을_한다();
        String loginToken = loginResponse.getToken();
        코린.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

        코린.로그인한_상태로(loginToken).팔로우한다(followingId);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다(
                "/api/v1/members/me/followings?page=0&size=1&query=O&careerLevel=senior&jobType=backend", loginToken);

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);

        Member following = 오찌.엔티티를().추가정보와_인벤토리를_추가해서_생성(followingLoginResponse.getMember().getId(), SENIOR, BACKEND,
                List.of(인벤토리_엔티티로_변환한다(inventoryProductResponse, followingId)));
        MemberWithProfileProductResponse followingResponse = MemberWithProfileProductResponse.of(following, true);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems())
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("followerCount")
                        .hasSize(1)
                        .containsExactly(followingResponse)
        );
    }

    @Test
    void 재로그인_시_팔로워_수가_변하지_않는다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        LoginResponse followingResponse = 민초.로그인을_한다();
        민초.로그인한_상태로(followingResponse.getToken()).추가정보를_입력한다(memberRequest);

        final LoginResponse followerResponse = 코린.로그인을_한다();
        코린.로그인한_상태로(followerResponse.getToken()).추가정보를_입력한다(memberRequest);
        코린.로그인한_상태로(followerResponse.getToken()).팔로우한다(followingResponse.getMember().getId());

        final LoginResponse reLoggedInFollowingResponse = 민초.로그인을_한다();

        // when
        final LoggedInMemberResponse followingMemberResponse = 민초.로그인한_상태로(reLoggedInFollowingResponse.getToken()).자신의_프로필을_조회한다()
                .as(LoggedInMemberResponse.class);

        // then
        assertThat(followingMemberResponse.getFollowerCount()).isOne();
    }

    private Product 제품을_저장한다(Product product) {
        return productRepository.save(product);
    }

    private InventoryProduct 인벤토리_엔티티로_변환한다(InventoryProductResponse inventoryProductResponse, Long memberId) {
        return InventoryProduct.builder()
                .product(제품_엔티티로_변환한다(inventoryProductResponse.getProduct()))
                .selected(true)
                .member(Member.builder()
                        .id(memberId)
                        .build())
                .build();
    }

    private Product 제품_엔티티로_변환한다(ProductResponse productResponse) {
        return Product.builder()
                .id(productResponse.getId())
                .name(productResponse.getName())
                .category(productResponse.getCategory().toCategory())
                .rating(productResponse.getRating())
                .imageUrl(productResponse.getImageUrl())
                .reviewCount(productResponse.getReviewCount())
                .build();
    }

    private InventoryProductsResponse 자신의_인벤토리_장비를_조회한다(final LoginResponse secondLoginResponse) {
        return 코린.로그인한_상태로(secondLoginResponse.getToken()).자신의_인벤토리를_조회한다().as(InventoryProductsResponse.class);
    }
}
