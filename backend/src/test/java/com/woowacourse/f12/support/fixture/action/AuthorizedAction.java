package com.woowacourse.f12.support.fixture.action;

import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_PATCH_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_POST_요청을_보낸다;

import com.woowacourse.f12.dto.request.inventoryproduct.ProfileProductRequest;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.support.fixture.ReviewFixture;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;

public class AuthorizedAction {

    private final String token;

    public AuthorizedAction(final String token) {
        this.token = token;
    }

    public ExtractableResponse<Response> 추가정보를_입력한다(final MemberRequest memberRequest) {
        return 로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);
    }

    public ExtractableResponse<Response> 자신의_프로필을_조회한다() {
        return 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/me", token);
    }

    public ExtractableResponse<Response> 팔로우한다(final Long targetId) {
        return 로그인된_상태로_POST_요청을_보낸다("/api/v1/members/" + targetId + "/following", token);
    }

    public ExtractableResponse<Response> 리뷰를_작성한다(final Long productId, final ReviewFixture reviewFixture) {
        return reviewFixture.작성_요청을_보낸다(productId, token);
    }

    public ExtractableResponse<Response> 자신의_인벤토리를_조회한다() {
        return 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/inventoryProducts", token);
    }

    public ExtractableResponse<Response> 대표장비를_등록한다(final List<Long> inventoryProductIds) {
        return 로그인된_상태로_PATCH_요청을_보낸다(
                "api/v1/members/inventoryProducts", token,
                new ProfileProductRequest(inventoryProductIds));
    }
}
