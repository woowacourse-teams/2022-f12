package com.woowacourse.f12.application.auth.token;

import com.woowacourse.f12.domain.member.Role;
import java.util.Objects;
import lombok.Getter;

@Getter
public class MemberPayload {

    private final Long id;
    private final Role role;

    public MemberPayload(final Long id, final Role role) {
        this.id = id;
        this.role = role;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MemberPayload that = (MemberPayload) o;
        return Objects.equals(id, that.id) && role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role);
    }
}
