package com.woowacourse.f12.acceptance;

import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_DELETE_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_PATCH_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_POST_요청을_보낸다;
import static com.woowacourse.f12.domain.member.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.JUNIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.SENIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.BACKEND_CONSTANT;
import static com.woowacourse.f12.support.fixture.AcceptanceFixture.민초;
import static com.woowacourse.f12.support.fixture.AcceptanceFixture.코린;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.response.auth.LoginMemberResponse;
import com.woowacourse.f12.dto.response.auth.LoginResponse;
import com.woowacourse.f12.dto.response.member.LoggedInMemberResponse;
import com.woowacourse.f12.dto.response.member.MemberResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class MemberAcceptanceTest extends AcceptanceTest {

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
    void 재로그인_시_팔로워_수가_변하지_않는다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        LoginResponse followingResponse = 민초.로그인을_한다();
        민초.로그인한_상태로(followingResponse.getToken()).추가정보를_입력한다(memberRequest);

        LoginResponse followerResponse = 코린.로그인을_한다();
        코린.로그인한_상태로(followerResponse.getToken()).추가정보를_입력한다(memberRequest);
        코린.로그인한_상태로(followerResponse.getToken()).팔로우한다(followingResponse.getMember().getId());

        LoginResponse reLoggedInFollowingResponse = 민초.로그인을_한다();

        // when
        LoggedInMemberResponse followingMemberResponse = 민초.로그인한_상태로(reLoggedInFollowingResponse.getToken())
                .자신의_프로필을_조회한다()
                .as(LoggedInMemberResponse.class);

        // then
        assertThat(followingMemberResponse.getFollowerCount()).isOne();
    }
}
