package com.woowacourse.f12.presentation.auth;

import com.woowacourse.f12.domain.member.Role;
import com.woowacourse.f12.exception.unauthorized.TokenInvalidException;
import lombok.Getter;

@Getter
public class LoginPayload {

    private static final String PAYLOAD_DELIMITER = ";";
    private static final int ID_INDEX = 0;
    private static final int ROLE_INDEX = 1;
    private static final int PAYLOAD_SIZE = 2;

    private final Long id;
    private final Role role;

    private LoginPayload(final Long id, final Role role) {
        this.id = id;
        this.role = role;
    }

    public static LoginPayload from(final String payload) {
        String[] splitPayload = payload.split(PAYLOAD_DELIMITER);
        validateSize(splitPayload);
        return parse(splitPayload);
    }

    private static void validateSize(final String[] splitPayload) {
        if (splitPayload.length != PAYLOAD_SIZE) {
            throw new TokenInvalidException();
        }
    }

    private static LoginPayload parse(final String[] splitPayload) {
        try {
            Long id = Long.parseLong(splitPayload[ID_INDEX]);
            Role role = Role.valueOf(splitPayload[ROLE_INDEX]);
            return new LoginPayload(id, role);
        } catch (IllegalArgumentException e) {
            throw new TokenInvalidException();
        }
    }
}
