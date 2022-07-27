package com.woowacourse.f12.dto.response.member;

import com.woowacourse.f12.domain.member.Member;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class MemberPageResponse {

    private boolean hasNext;
    private List<MemberWithProfileProductResponse> items;

    public MemberPageResponse() {
    }

    private MemberPageResponse(final boolean hasNext, final List<MemberWithProfileProductResponse> items) {
        this.hasNext = hasNext;
        this.items = items;
    }

    public static MemberPageResponse from(final Slice<Member> slice) {
        final List<MemberWithProfileProductResponse> items = slice.getContent()
                .stream()
                .map(MemberWithProfileProductResponse::from)
                .collect(Collectors.toList());
        return new MemberPageResponse(slice.hasNext(), items);
    }
}
