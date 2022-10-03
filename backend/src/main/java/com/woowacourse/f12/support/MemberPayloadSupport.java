package com.woowacourse.f12.support;

import com.woowacourse.f12.application.auth.token.MemberPayload;
import javax.annotation.Nullable;

public class MemberPayloadSupport {

    private MemberPayloadSupport() {
    }

    public static Long getLoggedInMemberId(@Nullable final MemberPayload loggedInMemberPayload) {
        if (loggedInMemberPayload == null) {
            return null;
        }
        return loggedInMemberPayload.getId();
    }
}
