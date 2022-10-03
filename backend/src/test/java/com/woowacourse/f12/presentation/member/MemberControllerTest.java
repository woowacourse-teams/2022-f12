package com.woowacourse.f12.presentation.member;

import static com.woowacourse.f12.presentation.member.CareerLevelConstant.JUNIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.NONE_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.BACKEND_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.ETC_CONSTANT;
import static com.woowacourse.f12.support.fixture.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.fixture.MemberFixture.CORINNE;
import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_1;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.refEq;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.f12.application.auth.JwtProvider;
import com.woowacourse.f12.application.member.MemberService;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.member.Following;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.request.member.MemberSearchRequest;
import com.woowacourse.f12.dto.response.member.LoggedInMemberResponse;
import com.woowacourse.f12.dto.response.member.MemberPageResponse;
import com.woowacourse.f12.dto.response.member.MemberResponse;
import com.woowacourse.f12.exception.badrequest.AlreadyFollowingException;
import com.woowacourse.f12.exception.badrequest.InvalidFollowerCountException;
import com.woowacourse.f12.exception.badrequest.NotFollowingException;
import com.woowacourse.f12.exception.badrequest.SelfFollowException;
import com.woowacourse.f12.exception.notfound.MemberNotFoundException;
import com.woowacourse.f12.presentation.PresentationTest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MemberController.class)
class MemberControllerTest extends PresentationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtProvider jwtProvider;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 로그인된_상태에서_나의_회원정보를_조회_성공() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        given(memberService.findLoggedInMember(1L))
                .willReturn(LoggedInMemberResponse.from(CORINNE.생성(1L)));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members/me")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("members-get-own"))
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
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
                .andDo(document("members-get-by-memberId"))
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
                .willReturn(loggedInId.toString());
        given(jwtProvider.validateToken(authorizationHeader))
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
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(memberService).find(targetId, loggedInId)
        );
    }

    @Test
    void 로그인된_상태에서_나의_회원정보를_수정_성공() throws Exception {
        // given
        MemberRequest memberRequest = new MemberRequest(JUNIOR_CONSTANT, ETC_CONSTANT);
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
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
                .andDo(document("members-update"))
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
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
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
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
                () -> verify(jwtProvider).validateToken(authorizationHeader),
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
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
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
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(memberService, times(0)).updateMember(eq(1L), any(MemberRequest.class))
        );
    }

    @Test
    void 로그인된_상태에서_나의_회원정보를_수정_실패_DTO_필드가_null인_경우() throws Exception {
        // given
        MemberRequest memberRequest = new MemberRequest(null, null);
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
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
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(memberService, times(0)).updateMember(eq(1L), any(MemberRequest.class))
        );
    }

    @Test
    void 비회원이_키워드와_옵션으로_회원을_조회한다() throws Exception {
        // given
        MemberSearchRequest memberSearchRequest = new MemberSearchRequest("cheese", NONE_CONSTANT, BACKEND_CONSTANT);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(CORINNE.생성(1L),
                KEYBOARD_1.생성(1L));
        Member member = CORINNE.인벤토리를_추가해서_생성(1L, List.of(inventoryProduct));

        MemberPageResponse memberPageResponse =
                MemberPageResponse.ofByFollowingCondition(new SliceImpl<>(List.of(member), pageable, false),
                        false);
        given(memberService.findBySearchConditions(isNull(), any(MemberSearchRequest.class), any(PageRequest.class)))
                .willReturn(memberPageResponse);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members?query=cheese&careerLevel=none&jobType=backend&page=0&size=10")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("members-search"))
                .andDo(print());

        verify(memberService).findBySearchConditions(isNull(), refEq(memberSearchRequest), refEq(pageable));
    }

    @Test
    void 회원이_키워드와_옵션으로_회원을_조회한다() throws Exception {
        // given
        MemberSearchRequest memberSearchRequest = new MemberSearchRequest("cheese", NONE_CONSTANT, BACKEND_CONSTANT);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(CORINNE.생성(1L),
                KEYBOARD_1.생성(1L));
        Member member = CORINNE.인벤토리를_추가해서_생성(1L, List.of(inventoryProduct));
        Long loggedInId = 2L;
        Following following = Following.builder()
                .followerId(loggedInId)
                .followingId(member.getId())
                .build();
        MemberPageResponse memberPageResponse = MemberPageResponse.of(
                new SliceImpl<>(List.of(member), pageable, false), List.of(following));
        String authorizationHeader = "Bearer Token";

        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(loggedInId.toString());
        given(memberService.findBySearchConditions(eq(loggedInId), any(MemberSearchRequest.class),
                any(PageRequest.class)))
                .willReturn(memberPageResponse);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members?query=cheese&careerLevel=none&jobType=backend&page=0&size=10")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(memberService).findBySearchConditions(eq(loggedInId), refEq(memberSearchRequest),
                        refEq(pageable))
        );
    }

    @Test
    void 회원_조회_실패_옵션값이_올바르지_않을때() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members?query=cheese&careerLevel=invalid&jobType=invalid&page=0&size=10")
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(memberService, times(0)).findBySearchConditions(isNull(), any(MemberSearchRequest.class),
                any(PageRequest.class));
    }

    @Test
    void 회원_조회_성공_키워드와_옵션값이_주어지지_않을때() throws Exception {
        // given
        MemberSearchRequest memberSearchRequest = new MemberSearchRequest(null, null, null);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(CORINNE.생성(1L),
                KEYBOARD_1.생성(1L));
        Member member = CORINNE.인벤토리를_추가해서_생성(1L, List.of(inventoryProduct));

        MemberPageResponse memberPageResponse =
                MemberPageResponse.ofByFollowingCondition(new SliceImpl<>(List.of(member), pageable, false),
                        false);
        given(memberService.findBySearchConditions(isNull(), any(MemberSearchRequest.class), any(PageRequest.class)))
                .willReturn(memberPageResponse);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members?page=0&size=10")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());

        verify(memberService).findBySearchConditions(isNull(), refEq(memberSearchRequest), refEq(pageable));
    }

    @Test
    void 팔로우_성공() throws Exception {
        // given
        Long followerId = 1L;
        Long followingId = 2L;

        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(followerId.toString());

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/" + followingId + "/following")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isNoContent())
                .andDo(document("follow"))
                .andDo(print());

        verify(memberService).follow(followerId, followingId);
    }

    @Test
    void 팔로우_실패_자기_자신을_팔로우() throws Exception {
        // given
        Long followerId = 1L;
        Long followingId = 1L;

        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(followerId.toString());
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
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(followerId.toString());
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
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(followerId.toString());
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
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(followerId.toString());
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
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(followerId.toString());
        willDoNothing().given(memberService)
                .unfollow(followerId, followingId);

        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/v1/members/" + followingId + "/following")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isNoContent())
                .andDo(document("unfollow"))
                .andDo(print());

        verify(memberService).unfollow(followerId, followingId);
    }

    @Test
    void 언팔로우_실패_팔로워_또는_팔로이가_없음() throws Exception {
        // given
        Long followerId = 1L;
        Long followingId = 2L;

        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(followerId.toString());
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
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(followerId.toString());
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

    @Test
    void 언팔로우_실패_대상의_팔로워_수가_0_이하인_경우() throws Exception {
        // given
        Long followerId = 1L;
        Long followingId = 2L;

        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(followerId.toString());
        willThrow(new InvalidFollowerCountException())
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

    @Test
    void 팔로잉하는_회원_목록_조회_성공() throws Exception {
        // given
        Long loggedInId = 1L;
        MemberSearchRequest memberSearchRequest = new MemberSearchRequest(null, null, null);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

        MemberPageResponse memberPageResponse =
                MemberPageResponse.ofByFollowingCondition(new SliceImpl<>(List.of(CORINNE.생성(2L)), pageable, false),
                        false);

        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(loggedInId.toString());
        given(memberService.findFollowingsByConditions(eq(loggedInId), refEq(memberSearchRequest), eq(pageable)))
                .willReturn(memberPageResponse);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members/me/followings?page=0&size=10")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("search-followings"))
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(memberService).findFollowingsByConditions(eq(loggedInId), refEq(memberSearchRequest),
                        eq(pageable))
        );
    }
}
