package com.woowacourse.f12.acceptance;

import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_DELETE_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_PATCH_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_POST_요청을_보낸다;
import static com.woowacourse.f12.domain.member.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.member.CareerLevel.SENIOR;
import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.JUNIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.SENIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.BACKEND_CONSTANT;
import static com.woowacourse.f12.support.fixture.AcceptanceFixture.CORINNE;
import static com.woowacourse.f12.support.fixture.AcceptanceFixture.MINCHO;
import static com.woowacourse.f12.support.fixture.AcceptanceFixture.OHZZI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.response.auth.LoginMemberResponse;
import com.woowacourse.f12.dto.response.auth.LoginResponse;
import com.woowacourse.f12.dto.response.member.MemberPageResponse;
import com.woowacourse.f12.dto.response.member.MemberResponse;
import com.woowacourse.f12.dto.response.member.MemberWithProfileProductResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class MemberAcceptanceTest extends AcceptanceTest {

    @Test
    void 로그인_된_상태에서_내_회원정보를_업데이트한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(JUNIOR_CONSTANT, BACKEND_CONSTANT);
        LoginResponse loginResponse = CORINNE.로그인을_한다();
        String loginToken = loginResponse.getToken();

        // when
        ExtractableResponse<Response> memberUpdatedResponse = 로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", loginToken,
                memberRequest);

        // then
        ExtractableResponse<Response> memberGetResponse = CORINNE.로그인한_상태로(loginToken).자신의_프로필을_조회한다();
        Member expectedMember = CORINNE.객체를().추가정보를_입력하여_생성(loginResponse.getMember().getId(), JUNIOR, BACKEND);

        assertAll(
                () -> assertThat(memberUpdatedResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberGetResponse.as(MemberResponse.class))
                        .usingRecursiveComparison()
                        .isEqualTo(MemberResponse.from(expectedMember, false))
        );
    }

    @Test
    void 로그인_된_상태에서_내_회원정보를_조회한다() {
        // given
        LoginResponse loginResponse = CORINNE.로그인을_한다();
        String loginToken = loginResponse.getToken();

        MemberRequest memberRequest = new MemberRequest(JUNIOR_CONSTANT, BACKEND_CONSTANT);
        CORINNE.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/me", loginToken);

        // then
        Long loggedInMemberId = loginResponse.getMember().getId();
        Member expectedMember = CORINNE.객체를().추가정보를_입력하여_생성(loggedInMemberId, JUNIOR, BACKEND);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(MemberResponse.class)).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.from(expectedMember, false))
        );
    }

    @Test
    void 로그인_된_상태에서_추가정보가_입력되지_않았을떄_내_회원정보를_조회한다() {
        // given
        LoginResponse loginResponse = CORINNE.로그인을_한다();

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/me", loginResponse.getToken());

        // then
        Member expectedMember = CORINNE.객체를().추가정보_없이_생성(loginResponse.getMember().getId());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(MemberResponse.class)).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.from(expectedMember, false))
        );
    }

    @Test
    void 비로그인_상태에서_회원정보를_조회한다() {
        // given
        LoginResponse loginResponse = CORINNE.로그인을_한다();

        String loginToken = loginResponse.getToken();
        LoginMemberResponse loginMemberResponse = loginResponse.getMember();
        MemberRequest memberRequest = new MemberRequest(JUNIOR_CONSTANT, BACKEND_CONSTANT);
        CORINNE.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

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
        MemberRequest memberRequest = new MemberRequest(JUNIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse firstLoginResponse = MINCHO.로그인을_한다();
        Long targetId = firstLoginResponse.getMember().getId();
        MINCHO.로그인한_상태로(firstLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse secondLoginResponse = CORINNE.로그인을_한다();
        String loginToken = secondLoginResponse.getToken();
        CORINNE.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);
        CORINNE.로그인한_상태로(loginToken).팔로우한다(targetId);

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
                        .isEqualTo(MemberResponse.from(expectedMember, true))
        );
    }

    @Test
    void 로그인_상태에서_팔로우하지_않은_회원의_정보를_조회한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(JUNIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse firstLoginResponse = MINCHO.로그인을_한다();
        Long targetId = firstLoginResponse.getMember().getId();
        MINCHO.로그인한_상태로(firstLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse secondLoginResponse = CORINNE.로그인을_한다();
        String loginToken = secondLoginResponse.getToken();
        CORINNE.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

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
                        .isEqualTo(MemberResponse.from(expectedMember, false))
        );
    }

    @Test
    void 비회원이_회원목록을_키워드와_옵션을_입력하지않고_조회한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse firstLoginResponse = MINCHO.로그인을_한다();
        MINCHO.로그인한_상태로(firstLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse secondLoginResponse = CORINNE.로그인을_한다();
        CORINNE.로그인한_상태로(secondLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/members?page=0&size=2");

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);

        Member member1 = MINCHO.객체를().추가정보를_입력하여_생성(firstLoginResponse.getMember().getId(), SENIOR, BACKEND);
        Member member2 = CORINNE.객체를().추가정보를_입력하여_생성(secondLoginResponse.getMember().getId(), SENIOR, BACKEND);

        MemberWithProfileProductResponse expectedMemberResponse1 =
                MemberWithProfileProductResponse.of(member1, false);
        MemberWithProfileProductResponse expectedMemberResponse2 =
                MemberWithProfileProductResponse.of(member2, false);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems())
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields()
                        .hasSize(2)
                        .containsExactly(expectedMemberResponse2, expectedMemberResponse1)
        );
    }

    @Test
    void 비회원이_회원목록을_키워드와_옵션을_입력하지않고_조회할때_추가정보가_입력되지않은_회원은_포함되지_않는다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        MINCHO.로그인을_한다();

        LoginResponse secondLoginResponse = CORINNE.로그인을_한다();
        CORINNE.로그인한_상태로(secondLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/members?page=0&size=2");

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);

        Member member = CORINNE.객체를().추가정보를_입력하여_생성(secondLoginResponse.getMember().getId(), SENIOR, BACKEND);
        MemberWithProfileProductResponse expectedMemberResponse = MemberWithProfileProductResponse.of(member, false);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems())
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields()
                        .hasSize(1)
                        .contains(expectedMemberResponse)
        );
    }

    @Test
    void 비회원이_회원목록을_옵션으로_검색하여_조회한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse firstLoginResponse = MINCHO.로그인을_한다();
        MINCHO.로그인한_상태로(firstLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse secondLoginResponse = CORINNE.로그인을_한다();
        CORINNE.로그인한_상태로(secondLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/members?page=0&size=2&careerLevel=senior&jobType=backend");

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);

        Member member1 = MINCHO.객체를().추가정보를_입력하여_생성(firstLoginResponse.getMember().getId(), SENIOR, BACKEND);
        Member member2 = CORINNE.객체를().추가정보를_입력하여_생성(secondLoginResponse.getMember().getId(), SENIOR, BACKEND);
        MemberWithProfileProductResponse expectedMemberResponse1 = MemberWithProfileProductResponse.of(member1, false);
        MemberWithProfileProductResponse expectedMemberResponse2 = MemberWithProfileProductResponse.of(member2, false);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(
                        memberPageResponse.getItems()).usingRecursiveFieldByFieldElementComparatorIgnoringFields()
                        .hasSize(2)
                        .containsExactly(expectedMemberResponse2, expectedMemberResponse1)
        );
    }

    @Test
    void 비회원이_회원목록을_키워드와_옵션으로_검색하여_조회한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        MINCHO.로그인을_하고().추가정보를_입력한다(memberRequest);

        LoginResponse secondLoginResponse = CORINNE.로그인을_한다();
        CORINNE.로그인한_상태로(secondLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/members?page=0&size=2&query=cheese&careerLevel=senior&jobType=backend");

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);

        Member member = CORINNE.객체를().추가정보를_입력하여_생성(secondLoginResponse.getMember().getId(), SENIOR, BACKEND);
        MemberWithProfileProductResponse expectedMemberResponse = MemberWithProfileProductResponse.of(member, false);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems())
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields()
                        .hasSize(1)
                        .containsOnly(expectedMemberResponse)
        );
    }

    @Test
    void 회원이_회원목록을_검색한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse firstLoginResponse = OHZZI.로그인을_한다();
        OHZZI.로그인한_상태로(firstLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse secondLoginResponse = MINCHO.로그인을_한다();
        MINCHO.로그인한_상태로(secondLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse thirdLoginResponse = CORINNE.로그인을_한다();
        String loginToken = thirdLoginResponse.getToken();
        CORINNE.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

        CORINNE.로그인한_상태로(loginToken).팔로우한다(secondLoginResponse.getMember().getId());

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members?page=0&size=2", loginToken);

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);
        Member member1 = MINCHO.객체를().추가정보를_입력하여_생성(secondLoginResponse.getMember().getId(), SENIOR, BACKEND);
        Member member2 = CORINNE.객체를().추가정보를_입력하여_생성(thirdLoginResponse.getMember().getId(), SENIOR, BACKEND);
        MemberWithProfileProductResponse expectedMemberResponse1 = MemberWithProfileProductResponse.of(member1, true);
        MemberWithProfileProductResponse expectedMemberResponse2 = MemberWithProfileProductResponse.of(member2, false);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isTrue(),
                () -> assertThat(memberPageResponse.getItems())
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("followerCount")
                        .hasSize(2)
                        .containsExactly(expectedMemberResponse2, expectedMemberResponse1)
        );
    }

    @Test
    void 회원목록은_id의_역순으로_조회된다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse firstLoginResponse = MINCHO.로그인을_한다();
        MINCHO.로그인한_상태로(firstLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse secondLoginResponse = CORINNE.로그인을_한다();
        CORINNE.로그인한_상태로(secondLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

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
    void 다른_회원을_팔로우한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse firstLoginResponse = MINCHO.로그인을_한다();
        Long targetId = firstLoginResponse.getMember().getId();
        MINCHO.로그인한_상태로(firstLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse secondLoginResponse = CORINNE.로그인을_한다();
        String loginToken = secondLoginResponse.getToken();
        CORINNE.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_POST_요청을_보낸다("/api/v1/members/" + targetId + "/following",
                loginToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 팔로우한_회원을_언팔로우한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse firstLoginResponse = MINCHO.로그인을_한다();
        Long targetId = firstLoginResponse.getMember().getId();
        MINCHO.로그인한_상태로(firstLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse secondLoginResponse = CORINNE.로그인을_한다();
        String loginToken = secondLoginResponse.getToken();
        CORINNE.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

        CORINNE.로그인한_상태로(loginToken).팔로우한다(targetId);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_DELETE_요청을_보낸다("/api/v1/members/" + targetId + "/following",
                loginToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 팔로우하는_회원의_목록을_조회한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse followeeLoginResponse = OHZZI.로그인을_한다();
        Long followeeId = followeeLoginResponse.getMember().getId();
        OHZZI.로그인한_상태로(followeeLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse notFolloweeLoginResponse = MINCHO.로그인을_한다();
        MINCHO.로그인한_상태로(notFolloweeLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse loginResponse = CORINNE.로그인을_한다();
        String loginToken = loginResponse.getToken();
        CORINNE.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

        CORINNE.로그인한_상태로(loginToken).팔로우한다(followeeId);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/me/followees?page=0&size=1",
                loginToken);

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);

        Member followee = OHZZI.객체를().추가정보를_입력하여_생성(followeeLoginResponse.getMember().getId(), SENIOR, BACKEND);
        MemberWithProfileProductResponse followeeResponse = MemberWithProfileProductResponse.of(followee, true);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems())
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("followerCount")
                        .hasSize(1)
                        .containsExactly(followeeResponse)
        );
    }

    @Test
    void 팔로우하는_회원의_목록을_검색하여_조회한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse followeeLoginResponse = OHZZI.로그인을_한다();
        Long followeeId = followeeLoginResponse.getMember().getId();
        OHZZI.로그인한_상태로(followeeLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse notFolloweeLoginResponse = MINCHO.로그인을_한다();
        MINCHO.로그인한_상태로(notFolloweeLoginResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse loginResponse = CORINNE.로그인을_한다();
        String loginToken = loginResponse.getToken();
        CORINNE.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

        CORINNE.로그인한_상태로(loginToken).팔로우한다(followeeId);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다(
                "/api/v1/members/me/followees?page=0&size=1&query=O&careerLevel=senior&jobType=backend", loginToken);

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);

        Member followee = OHZZI.객체를().추가정보를_입력하여_생성(followeeLoginResponse.getMember().getId(), SENIOR, BACKEND);
        MemberWithProfileProductResponse followeeResponse = MemberWithProfileProductResponse.of(followee, true);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems())
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("followerCount")
                        .hasSize(1)
                        .containsExactly(followeeResponse)
        );
    }
}
