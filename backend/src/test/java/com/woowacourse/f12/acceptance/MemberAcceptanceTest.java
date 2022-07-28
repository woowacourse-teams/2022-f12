package com.woowacourse.f12.acceptance;

import static com.woowacourse.f12.acceptance.support.LoginUtil.로그인을_한다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_PATCH_요청을_보낸다;
import static com.woowacourse.f12.dto.CareerLevelConstant.JUNIOR;
import static com.woowacourse.f12.dto.CareerLevelConstant.SENIOR;
import static com.woowacourse.f12.dto.JobTypeConstant.BACKEND;
import static com.woowacourse.f12.support.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_1;
import static com.woowacourse.f12.support.MemberFixtures.CORINNE;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_5;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.domain.member.JobType;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.product.Keyboard;
import com.woowacourse.f12.domain.product.KeyboardRepository;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.response.auth.LoginMemberResponse;
import com.woowacourse.f12.dto.response.auth.LoginResponse;
import com.woowacourse.f12.dto.response.member.MemberPageResponse;
import com.woowacourse.f12.dto.response.member.MemberResponse;
import com.woowacourse.f12.dto.response.member.MemberWithProfileProductResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class MemberAcceptanceTest extends AcceptanceTest {

    @Autowired
    private KeyboardRepository keyboardRepository;

    @Test
    void 로그인_된_상태에서_내_회원정보를_업데이트한다() {
        // given
        LoginResponse loginResponse = 로그인을_한다("1");
        String token = loginResponse.getToken();
        MemberRequest memberRequest = new MemberRequest(JUNIOR, BACKEND);

        // when
        ExtractableResponse<Response> memberUpdatedResponse = 로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token,
                memberRequest);
        ExtractableResponse<Response> memberGetResponse = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/me", token);

        // then
        Member member = Member.builder()
                .careerLevel(CareerLevel.JUNIOR)
                .jobType(JobType.BACKEND)
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

        MemberRequest memberRequest = new MemberRequest(JUNIOR, BACKEND);
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/me", token);

        // then
        Member expectedMember = loginMemberResponse.toMember();
        expectedMember.updateCareerLevel(CareerLevel.JUNIOR);
        expectedMember.updateJobType(JobType.BACKEND);

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

        MemberRequest memberRequest = new MemberRequest(JUNIOR, BACKEND);
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/members/" + loginMemberResponse.getId());

        // then
        Member expectedMember = Member.builder()
                .id(loginMemberResponse.getId())
                .name(loginMemberResponse.getName())
                .gitHubId(loginMemberResponse.getGitHubId())
                .imageUrl(loginMemberResponse.getImageUrl())
                .careerLevel(CareerLevel.JUNIOR)
                .jobType(JobType.BACKEND)
                .build();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(MemberResponse.class)).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.from(expectedMember))
        );
    }

    @Test
    void 회원정보를_검색하여_조회한다() {
        // given
        Keyboard keyboard = 키보드를_저장한다(KEYBOARD_1.생성());
        MemberRequest memberRequest = new MemberRequest(SENIOR, BACKEND);
        LoginResponse loginResponse1 = 로그인을_한다("1");
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", loginResponse1.getToken(), memberRequest);
        LoginResponse loginResponse = 로그인을_한다("hamcheeseburger");
        String token = loginResponse.getToken();
        Long memberId = loginResponse.getMember().getId();
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);
        REVIEW_RATING_5.작성_요청을_보낸다(keyboard.getId(), token);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/members?page=0&size=2");

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(1L, CORINNE.생성(memberId), keyboard);
        Member member = CORINNE.대표장비_추가(memberId, inventoryProduct);
        MemberWithProfileProductResponse memberWithProfileProductResponse = MemberWithProfileProductResponse.from(
                member);
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
    void 회원정보를_옵션으로_검색하여_조회한다() {
        // given
        Keyboard keyboard = 키보드를_저장한다(KEYBOARD_1.생성());
        MemberRequest memberRequest = new MemberRequest(SENIOR, BACKEND);
        LoginResponse loginResponse1 = 로그인을_한다("1");
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", loginResponse1.getToken(), memberRequest);
        LoginResponse loginResponse = 로그인을_한다("hamcheeseburger");
        String token = loginResponse.getToken();
        Long memberId = loginResponse.getMember().getId();
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);
        REVIEW_RATING_5.작성_요청을_보낸다(keyboard.getId(), token);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/members?page=0&size=2&careerLevel=senior&jobType=backend");

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(1L, CORINNE.생성(memberId), keyboard);
        Member member = CORINNE.대표장비_추가(memberId, inventoryProduct);
        MemberWithProfileProductResponse memberWithProfileProductResponse = MemberWithProfileProductResponse.from(
                member);
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
    void 회원정보를_키워드와_옵션으로_검색하여_조회한다() {
        // given
        Keyboard keyboard = 키보드를_저장한다(KEYBOARD_1.생성());
        MemberRequest memberRequest = new MemberRequest(SENIOR, BACKEND);
        LoginResponse loginResponse1 = 로그인을_한다("1");
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", loginResponse1.getToken(), memberRequest);
        LoginResponse loginResponse = 로그인을_한다("hamcheeseburger");
        String token = loginResponse.getToken();
        Long memberId = loginResponse.getMember().getId();
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);
        REVIEW_RATING_5.작성_요청을_보낸다(keyboard.getId(), token);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/members?page=0&size=2&query=cheese&careerLevel=senior&jobType=backend");

        // then
        MemberPageResponse memberPageResponse = response.as(MemberPageResponse.class);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(1L, CORINNE.생성(memberId), keyboard);
        Member member = CORINNE.대표장비_추가(memberId, inventoryProduct);
        MemberWithProfileProductResponse memberWithProfileProductResponse = MemberWithProfileProductResponse.from(
                member);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(
                        memberPageResponse.getItems()).usingRecursiveFieldByFieldElementComparatorIgnoringFields(
                                "profileProducts")
                        .hasSize(1)
                        .containsOnly(memberWithProfileProductResponse)
        );
    }

    private Keyboard 키보드를_저장한다(Keyboard keyboard) {
        return keyboardRepository.save(keyboard);
    }
}
