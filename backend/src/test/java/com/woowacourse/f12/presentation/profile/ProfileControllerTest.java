package com.woowacourse.f12.presentation.profile;

import static com.woowacourse.f12.exception.ErrorCode.EXPIRED_ACCESS_TOKEN;
import static com.woowacourse.f12.exception.ErrorCode.INVALID_PAGING_PARAM;
import static com.woowacourse.f12.exception.ErrorCode.INVALID_SEARCH_PARAM;
import static com.woowacourse.f12.exception.ErrorCode.INVALID_TOKEN_FORMAT;
import static com.woowacourse.f12.exception.ErrorCode.NOT_EXIST_ACCESS_TOKEN;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.NONE_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.BACKEND_CONSTANT;
import static com.woowacourse.f12.support.fixture.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.fixture.MemberFixture.CORINNE;
import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_1;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.f12.application.auth.token.MemberPayload;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.member.Following;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.Role;
import com.woowacourse.f12.domain.profile.Profiles;
import com.woowacourse.f12.dto.request.profile.ProfileSearchRequest;
import com.woowacourse.f12.dto.response.profile.PagedProfilesResponse;
import com.woowacourse.f12.presentation.PresentationTest;
import com.woowacourse.f12.support.ErrorCodeSnippet;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

class ProfileControllerTest extends PresentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 회원이_키워드와_옵션으로_프로필_목록을_조회한다() throws Exception {
        // given
        ProfileSearchRequest profileSearchRequest = new ProfileSearchRequest("cheese", NONE_CONSTANT, BACKEND_CONSTANT);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(CORINNE.생성(1L),
                KEYBOARD_1.생성(1L));
        Member member = CORINNE.인벤토리를_추가해서_생성(1L, List.of(inventoryProduct));
        Long loggedInId = 2L;
        Following following = Following.builder()
                .followerId(loggedInId)
                .followingId(member.getId())
                .build();
        PagedProfilesResponse memberPageResponse
                = PagedProfilesResponse.of(false,
                Profiles.of(List.of(member), List.of(inventoryProduct), List.of(following)));
        String authorizationHeader = "Bearer Token";

        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(loggedInId, Role.USER));
        given(profileService.findBySearchConditions(eq(loggedInId), any(ProfileSearchRequest.class),
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
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(profileService).findBySearchConditions(eq(loggedInId), refEq(profileSearchRequest),
                        refEq(pageable))
        );
    }

    @Test
    void 프로필_목록_조회_실패_옵션값이_올바르지_않을때() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members?query=cheese&careerLevel=invalid&jobType=invalid&page=0&size=10")
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(profileService, times(0)).findBySearchConditions(isNull(), any(ProfileSearchRequest.class),
                any(PageRequest.class));
    }

    @Test
    void 비회원이_키워드와_옵션으로_프로필_목록을_조회한다() throws Exception {
        // given
        ProfileSearchRequest profileSearchRequest = new ProfileSearchRequest("cheese", NONE_CONSTANT, BACKEND_CONSTANT);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(CORINNE.생성(1L),
                KEYBOARD_1.생성(1L));
        Member member = CORINNE.인벤토리를_추가해서_생성(1L, List.of(inventoryProduct));

        PagedProfilesResponse pagedProfilesResponse
                = PagedProfilesResponse.of(false,
                Profiles.of(List.of(member), List.of(inventoryProduct), Collections.emptyList()));
        given(profileService.findBySearchConditions(isNull(), any(ProfileSearchRequest.class), any(PageRequest.class)))
                .willReturn(pagedProfilesResponse);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members?query=cheese&careerLevel=none&jobType=backend&page=0&size=10")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("members-search",
                        new ErrorCodeSnippet(INVALID_SEARCH_PARAM, INVALID_PAGING_PARAM)))
                .andDo(print());

        verify(profileService).findBySearchConditions(isNull(), refEq(profileSearchRequest), refEq(pageable));
    }

    @Test
    void 프로필_목록_조회_성공_키워드와_옵션값이_주어지지_않을때() throws Exception {
        // given
        ProfileSearchRequest profileSearchRequest = new ProfileSearchRequest(null, null, null);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(CORINNE.생성(1L),
                KEYBOARD_1.생성(1L));
        Member member = CORINNE.인벤토리를_추가해서_생성(1L, List.of(inventoryProduct));

        PagedProfilesResponse pagedProfilesResponse
                = PagedProfilesResponse.of(false,
                Profiles.of(List.of(member), List.of(inventoryProduct), Collections.emptyList()));
        given(profileService.findBySearchConditions(isNull(), any(ProfileSearchRequest.class), any(PageRequest.class)))
                .willReturn(pagedProfilesResponse);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members?page=0&size=10")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());

        verify(profileService).findBySearchConditions(isNull(), refEq(profileSearchRequest), refEq(pageable));
    }

    @Test
    void 팔로잉하는_프로필_목록_조회_성공() throws Exception {
        // given
        Long loggedInId = 1L;
        ProfileSearchRequest profileSearchRequest = new ProfileSearchRequest(null, null, null);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(CORINNE.생성(2L),
                KEYBOARD_1.생성(1L));
        Member member = CORINNE.인벤토리를_추가해서_생성(2L, List.of(inventoryProduct));

        Following following = Following.builder()
                .followerId(loggedInId)
                .followingId(member.getId())
                .build();
        PagedProfilesResponse pagedProfilesResponse
                = PagedProfilesResponse.of(false,
                Profiles.of(List.of(member), List.of(inventoryProduct), List.of(following)));

        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(loggedInId, Role.USER));
        given(profileService.findFollowingsByConditions(eq(loggedInId), refEq(profileSearchRequest), eq(pageable)))
                .willReturn(pagedProfilesResponse);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members/me/followings?page=0&size=10")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("search-followings",
                        new ErrorCodeSnippet(NOT_EXIST_ACCESS_TOKEN, EXPIRED_ACCESS_TOKEN, INVALID_TOKEN_FORMAT)))
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(profileService).findFollowingsByConditions(eq(loggedInId), refEq(profileSearchRequest),
                        eq(pageable))
        );
    }
}
