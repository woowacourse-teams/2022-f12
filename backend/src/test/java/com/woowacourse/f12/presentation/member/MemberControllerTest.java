package com.woowacourse.f12.presentation.member;

import static com.woowacourse.f12.exception.ErrorCode.ALREADY_FOLLOWING;
import static com.woowacourse.f12.exception.ErrorCode.EMPTY_MEMBER_INFO_VALUE;
import static com.woowacourse.f12.exception.ErrorCode.EXPIRED_ACCESS_TOKEN;
import static com.woowacourse.f12.exception.ErrorCode.INVALID_MEMBER_INFO_VALUE;
import static com.woowacourse.f12.exception.ErrorCode.INVALID_TOKEN_FORMAT;
import static com.woowacourse.f12.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.woowacourse.f12.exception.ErrorCode.NOT_EXIST_ACCESS_TOKEN;
import static com.woowacourse.f12.exception.ErrorCode.NOT_FOLLOWING;
import static com.woowacourse.f12.exception.ErrorCode.REQUEST_DUPLICATED;
import static com.woowacourse.f12.exception.ErrorCode.SELF_FOLLOW;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.JUNIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.ETC_CONSTANT;
import static com.woowacourse.f12.support.fixture.MemberFixture.CORINNE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.f12.application.auth.token.JwtProvider;
import com.woowacourse.f12.application.auth.token.MemberPayload;
import com.woowacourse.f12.application.member.MemberService;
import com.woowacourse.f12.domain.member.Role;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.response.member.LoggedInMemberResponse;
import com.woowacourse.f12.dto.response.member.MemberResponse;
import com.woowacourse.f12.exception.badrequest.AlreadyFollowingException;
import com.woowacourse.f12.exception.badrequest.NotFollowingException;
import com.woowacourse.f12.exception.badrequest.SelfFollowException;
import com.woowacourse.f12.exception.notfound.MemberNotFoundException;
import com.woowacourse.f12.presentation.PresentationTest;
import com.woowacourse.f12.support.ErrorCodeSnippet;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

class MemberControllerTest extends PresentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private MemberService memberService;

    @Test
    void 로그인된_상태에서_나의_회원정보를_조회_성공() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.USER));
        given(memberService.findLoggedInMember(1L))
                .willReturn(LoggedInMemberResponse.from(CORINNE.생성(1L)));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members/me")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("members-get-own",
                        new ErrorCodeSnippet(NOT_EXIST_ACCESS_TOKEN, EXPIRED_ACCESS_TOKEN, INVALID_TOKEN_FORMAT)))
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(memberService).findLoggedInMember(1L)
        );
    }

    @Test
    void 비로그인_상태에서_회원정보를_조회_성공() throws Exception {
        // given
        Long memberId = 1L;
        given(memberService.find(memberId, null))
                .willReturn(MemberResponse.of(CORINNE.생성(memberId), false));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members/" + memberId)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("members-get-by-memberId",
                        new ErrorCodeSnippet(MEMBER_NOT_FOUND)))
                .andDo(print());

        verify(memberService).find(1L, null);
    }

    @Test
    void 비로그인_상태에서_회원정보를_조회_실패_등록되지_않은_회원일_경우() throws Exception {
        // given
        Long memberId = 1L;
        given(memberService.find(memberId, null))
                .willThrow(new MemberNotFoundException());

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members/" + memberId)
        );

        // then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());

        verify(memberService).find(1L, null);
    }

    @Test
    void 로그인된_상태에서_다른_회원의_정보_조회_성공() throws Exception {
        // given
        Long targetId = 1L;
        Long loggedInId = 2L;
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(loggedInId, Role.USER));
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(memberService.find(targetId, loggedInId))
                .willReturn(MemberResponse.of(CORINNE.생성(targetId), false));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members/" + targetId)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(memberService).find(targetId, loggedInId)
        );
    }

    @Test
    void 로그인된_상태에서_나의_회원정보를_수정_성공() throws Exception {
        // given
        MemberRequest memberRequest = new MemberRequest(JUNIOR_CONSTANT, ETC_CONSTANT);
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.USER));
        willDoNothing().given(memberService).updateMember(1L, memberRequest);

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/v1/members/me")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(memberRequest))
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("members-update",
                        new ErrorCodeSnippet(INVALID_MEMBER_INFO_VALUE, EMPTY_MEMBER_INFO_VALUE, NOT_EXIST_ACCESS_TOKEN,
                                EXPIRED_ACCESS_TOKEN, INVALID_TOKEN_FORMAT)))
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(memberService).updateMember(eq(1L), any(MemberRequest.class))
        );
    }

    @Test
    void 로그인된_상태에서_나의_회원정보를_수정_성공_Enum의_name값으로_요청을_보낼때() throws Exception {
        // given
        Map<String, Object> memberRequest = new HashMap<>();
        memberRequest.put("careerLevel", "senior");
        memberRequest.put("jobType", "backend");

        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.USER));
        willDoNothing().given(memberService).updateMember(eq(1L), any(MemberRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/v1/members/me")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .content(objectMapper.writeValueAsString(memberRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(memberService).updateMember(eq(1L), any(MemberRequest.class))
        );
    }

    @Test
    void 로그인된_상태에서_나의_회원정보를_수정_실패_Enum의_name값과_다른_요청일_경우() throws Exception {
        // given
        Map<String, Object> memberRequest = new HashMap<>();
        memberRequest.put("careerLevel", "invalid");
        memberRequest.put("jobType", "invalid");

        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.USER));
        willDoNothing().given(memberService).updateMember(eq(1L), any(MemberRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/v1/members/me")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .content(objectMapper.writeValueAsString(memberRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(memberService, times(0)).updateMember(eq(1L), any(MemberRequest.class))
        );
    }

    @Test
    void 로그인된_상태에서_나의_회원정보를_수정_실패_DTO_필드가_null인_경우() throws Exception {
        // given
        MemberRequest memberRequest = new MemberRequest(null, null);
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.USER));
        willDoNothing().given(memberService).updateMember(1L, memberRequest);

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/v1/members/me")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .content(objectMapper.writeValueAsString(memberRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(memberService, times(0)).updateMember(eq(1L), any(MemberRequest.class))
        );
    }

    @Test
    void 팔로우_성공() throws Exception {
        // given
        Long followerId = 1L;
        Long followingId = 2L;

        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(followerId, Role.USER));

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/" + followingId + "/following")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isNoContent())
                .andDo(document("follow",
                        new ErrorCodeSnippet(SELF_FOLLOW, ALREADY_FOLLOWING, REQUEST_DUPLICATED, MEMBER_NOT_FOUND)))
                .andDo(print());

        verify(memberService).follow(followerId, followingId);
    }

    @Test
    void 팔로우_실패_자기_자신을_팔로우() throws Exception {
        // given
        Long followerId = 1L;
        Long followingId = 1L;

        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(followerId, Role.USER));
        willThrow(new SelfFollowException())
                .given(memberService)
                .follow(followerId, followingId);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/" + followingId + "/following")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(memberService).follow(followerId, followingId);
    }

    @Test
    void 팔로우_실패_팔로워_또는_팔로이가_없음() throws Exception {
        // given
        Long followerId = 1L;
        Long followingId = 2L;

        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(followerId, Role.USER));
        willThrow(new MemberNotFoundException())
                .given(memberService)
                .follow(followerId, followingId);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/" + followingId + "/following")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());

        verify(memberService).follow(followerId, followingId);
    }

    @Test
    void 팔로우_실패_이미_팔로우_상태임() throws Exception {
        // given
        Long followerId = 1L;
        Long followingId = 2L;

        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(followerId, Role.USER));
        willThrow(new AlreadyFollowingException())
                .given(memberService)
                .follow(followerId, followingId);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/" + followingId + "/following")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(memberService).follow(followerId, followingId);
    }

    @Test
    void 팔로우_실패_중복된_팔로우_관계가_삽입되었음() throws Exception {
        // given
        Long followerId = 1L;
        Long followingId = 2L;

        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(followerId, Role.USER));
        willThrow(new DataIntegrityViolationException("데이터가 중복될 수 없습니다."))
                .given(memberService)
                .follow(followerId, followingId);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/" + followingId + "/following")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(memberService).follow(followerId, followingId);
    }

    @Test
    void 언팔로우_성공() throws Exception {
        // given
        Long followerId = 1L;
        Long followingId = 2L;

        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(followerId, Role.USER));
        willDoNothing().given(memberService)
                .unfollow(followerId, followingId);

        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/v1/members/" + followingId + "/following")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isNoContent())
                .andDo(document("unfollow",
                        new ErrorCodeSnippet(NOT_FOLLOWING, MEMBER_NOT_FOUND)))
                .andDo(print());

        verify(memberService).unfollow(followerId, followingId);
    }

    @Test
    void 언팔로우_실패_팔로워_또는_팔로이가_없음() throws Exception {
        // given
        Long followerId = 1L;
        Long followingId = 2L;

        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(followerId, Role.USER));
        willThrow(new MemberNotFoundException())
                .given(memberService)
                .unfollow(followerId, followingId);

        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/v1/members/" + followingId + "/following")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());

        verify(memberService).unfollow(followerId, followingId);
    }

    @Test
    void 언팔로우_실패_팔로우_상태가_아님() throws Exception {
        // given
        Long followerId = 1L;
        Long followingId = 2L;

        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(followerId, Role.USER));
        willThrow(new NotFollowingException())
                .given(memberService)
                .unfollow(followerId, followingId);

        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/v1/members/" + followingId + "/following")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(memberService).unfollow(followerId, followingId);
    }
}
