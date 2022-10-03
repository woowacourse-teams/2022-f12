package com.woowacourse.f12.presentation.member;

import com.woowacourse.f12.application.auth.token.MemberPayload;
import com.woowacourse.f12.application.member.MemberService;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.request.member.MemberSearchRequest;
import com.woowacourse.f12.dto.response.member.LoggedInMemberResponse;
import com.woowacourse.f12.dto.response.member.MemberPageResponse;
import com.woowacourse.f12.dto.response.member.MemberResponse;
import com.woowacourse.f12.presentation.auth.Login;
import com.woowacourse.f12.presentation.auth.VerifiedMember;
import com.woowacourse.f12.support.MemberPayloadSupport;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/me")
    @Login
    public ResponseEntity<LoggedInMemberResponse> showLoggedIn(@VerifiedMember final MemberPayload memberPayload) {
        final LoggedInMemberResponse loggedInMemberResponse = memberService.findLoggedInMember(memberPayload.getId());
        return ResponseEntity.ok(loggedInMemberResponse);
    }

    @GetMapping("/{memberId}")
    @Login(required = false)
    public ResponseEntity<MemberResponse> show(@PathVariable final Long memberId,
                                               @VerifiedMember @Nullable final MemberPayload loggedInMemberPayload) {
        Long loggedInMemberId = MemberPayloadSupport.getLoggedInMemberId(loggedInMemberPayload);
        final MemberResponse memberResponse = memberService.find(memberId, loggedInMemberId);
        return ResponseEntity.ok(memberResponse);
    }

    @PatchMapping("/me")
    @Login
    public ResponseEntity<Void> updateMe(@VerifiedMember final MemberPayload memberPayload,
                                         @Valid @RequestBody final MemberRequest memberRequest) {
        memberService.updateMember(memberPayload.getId(), memberRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Login(required = false)
    public ResponseEntity<MemberPageResponse> searchMembers(
            @VerifiedMember @Nullable final MemberPayload loggedInMemberPayload,
            @ModelAttribute final MemberSearchRequest memberSearchRequest,
            final Pageable pageable) {
        Long loggedInMemberId = MemberPayloadSupport.getLoggedInMemberId(loggedInMemberPayload);
        final MemberPageResponse memberPageResponse = memberService.findBySearchConditions(loggedInMemberId,
                memberSearchRequest, pageable);
        return ResponseEntity.ok(memberPageResponse);
    }

    @PostMapping("/{memberId}/following")
    @Login
    public ResponseEntity<Void> follow(@VerifiedMember final MemberPayload followerPayload,
                                       @PathVariable("memberId") final Long followingId) {
        memberService.follow(followerPayload.getId(), followingId);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/{memberId}/following")
    @Login
    public ResponseEntity<Void> unfollow(@VerifiedMember final MemberPayload followerPayload,
                                         @PathVariable("memberId") final Long followingId) {
        memberService.unfollow(followerPayload.getId(), followingId);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/me/followings")
    @Login
    public ResponseEntity<MemberPageResponse> searchFollowings(
            @VerifiedMember final MemberPayload loggedInMemberPayload,
            @ModelAttribute final MemberSearchRequest memberSearchRequest,
            final Pageable pageable) {
        return ResponseEntity.ok(
                memberService.findFollowingsByConditions(loggedInMemberPayload.getId(), memberSearchRequest, pageable));
    }
}
