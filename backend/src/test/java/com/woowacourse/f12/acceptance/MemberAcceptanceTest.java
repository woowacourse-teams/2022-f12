package com.woowacourse.f12.acceptance;

import static com.woowacourse.f12.acceptance.support.LoginUtil.로그인을_한다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_PATCH_요청을_보낸다;
import static com.woowacourse.f12.domain.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.JobType.BACK_END;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.domain.Member;
import com.woowacourse.f12.dto.request.MemberRequest;
import com.woowacourse.f12.dto.response.LoginMemberResponse;
import com.woowacourse.f12.dto.response.LoginResponse;
import com.woowacourse.f12.dto.response.MemberResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MemberAcceptanceTest extends AcceptanceTest {

    @Test
    void 로그인_된_상태에서_내_회원정보를_업데이트한다() {
        // given
        LoginResponse loginResponse = 로그인을_한다("1");
        String token = loginResponse.getToken();
        MemberRequest memberRequest = new MemberRequest(JUNIOR, BACK_END);

        // when
        ExtractableResponse<Response> memberUpdatedResponse = 로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token,
                memberRequest);
        ExtractableResponse<Response> memberGetResponse = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/me", token);

        // then
        Member member = Member.builder()
                .careerLevel(JUNIOR)
                .jobType(BACK_END)
                .build();
        assertAll(
                () -> assertThat(memberGetResponse.as(MemberResponse.class)).usingRecursiveComparison()
                        .comparingOnlyFields("careerLevel", "jobType")
                        .isEqualTo(MemberResponse.from(member)),
                () -> assertThat(memberUpdatedResponse.statusCode()).isEqualTo(HttpStatus.OK.value())
        );
    }

    @Test
    void 로그인_된_상태에서_내_회원정보를_조회한다() {
        // given
        LoginResponse loginResponse = 로그인을_한다("1");
        String token = loginResponse.getToken();
        LoginMemberResponse loginMemberResponse = loginResponse.getMember();

        MemberRequest memberRequest = new MemberRequest(JUNIOR, BACK_END);
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/me", token);

        // then
        Member expectedMember = loginMemberResponse.toMember();
        expectedMember.updateCareerLevel(JUNIOR);
        expectedMember.updateJobType(BACK_END);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(MemberResponse.class)).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.from(expectedMember))
        );
    }

    @Test
    void 비로그인_상태에서_회원정보를_조회한다() {
        // given
        LoginResponse loginResponse = 로그인을_한다("1");
        String token = loginResponse.getToken();
        LoginMemberResponse loginMemberResponse = loginResponse.getMember();

        MemberRequest memberRequest = new MemberRequest(JUNIOR, BACK_END);
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
                .jobType(BACK_END)
                .build();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(MemberResponse.class)).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.from(expectedMember))
        );
    }
}
