package com.woowacourse.f12.dto.response.profile;

import com.woowacourse.f12.domain.profile.Profiles;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class PagedProfilesResponse {

    private boolean hasNext;
    private List<ProfileResponse> items;

    private PagedProfilesResponse() {
    }

    private PagedProfilesResponse(final boolean hasNext, final List<ProfileResponse> items) {
        this.hasNext = hasNext;
        this.items = items;
    }

    public static PagedProfilesResponse empty() {
        return new PagedProfilesResponse(false, Collections.emptyList());
    }

    public static PagedProfilesResponse of(final boolean hasNext, final Profiles profiles) {
        final List<ProfileResponse> profileResponses = profiles.getProfiles()
                .stream()
                .map(ProfileResponse::from)
                .collect(Collectors.toList());
        return new PagedProfilesResponse(hasNext, profileResponses);
    }
}
