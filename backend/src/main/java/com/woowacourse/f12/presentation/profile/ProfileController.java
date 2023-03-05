package com.woowacourse.f12.presentation.profile;

import com.woowacourse.f12.application.auth.token.MemberPayload;
import com.woowacourse.f12.application.profile.ProfileService;
import com.woowacourse.f12.dto.request.member.MemberSearchRequest;
import com.woowacourse.f12.dto.response.profile.PagedProfilesResponse;
import com.woowacourse.f12.presentation.auth.Login;
import com.woowacourse.f12.presentation.auth.VerifiedMember;
import com.woowacourse.f12.support.MemberPayloadSupport;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/members")
@RestController
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(final ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    @Login(required = false)
    public PagedProfilesResponse search(@VerifiedMember @Nullable final MemberPayload loggedInMemberPayload,
                                        @ModelAttribute final MemberSearchRequest memberSearchRequest,
                                        final Pageable pageable) {
        Long loggedInMemberId = MemberPayloadSupport.getLoggedInMemberId(loggedInMemberPayload);
        return profileService.findBySearchConditions(loggedInMemberId, memberSearchRequest, pageable);
    }

    @GetMapping("/me/followings")
    @Login
    public PagedProfilesResponse searchByFollowings(@VerifiedMember final MemberPayload loggedInMemberPayload,
                                                    @ModelAttribute final MemberSearchRequest memberSearchRequest,
                                                    final Pageable pageable) {
        return profileService.findFollowingsByConditions(loggedInMemberPayload.getId(), memberSearchRequest, pageable);
    }
}
