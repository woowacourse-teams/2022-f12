package com.woowacourse.f12.presentation.member;

import com.woowacourse.f12.application.member.MemberService;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.request.member.MemberSearchRequest;
import com.woowacourse.f12.dto.response.member.LoggedInMemberResponse;
import com.woowacourse.f12.dto.response.member.MemberPageResponse;
import com.woowacourse.f12.dto.response.member.MemberResponse;
import com.woowacourse.f12.presentation.auth.LoginPayload;
import com.woowacourse.f12.presentation.auth.LoginRequired;
import com.woowacourse.f12.presentation.auth.VerifiedMember;
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
    @LoginRequired
    public ResponseEntity<LoggedInMemberResponse> showLoggedIn(
            @VerifiedMember final LoginPayload loginPayload) {
        final LoggedInMemberResponse loggedInMemberResponse = memberService.findLoggedInMember(
                loginPayload.getId());
        return ResponseEntity.ok(loggedInMemberResponse);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> show(@PathVariable final Long memberId,
                                               @VerifiedMember @Nullable final LoginPayload loginPayload) {
        final MemberResponse memberResponse = memberService.find(memberId, getNullableId(loginPayload));
        return ResponseEntity.ok(memberResponse);
    }

    @PatchMapping("/me")
    @LoginRequired
    public ResponseEntity<Void> updateMe(@VerifiedMember final LoginPayload loginPayload,
                                         @Valid @RequestBody final MemberRequest memberRequest) {
        memberService.updateMember(loginPayload.getId(), memberRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<MemberPageResponse> searchMembers(
            @VerifiedMember @Nullable final LoginPayload loginPayload,
            @ModelAttribute final MemberSearchRequest memberSearchRequest,
            final Pageable pageable) {
        final MemberPageResponse memberPageResponse = memberService.findByContains(getNullableId(loginPayload),
                memberSearchRequest,
                pageable);
        return ResponseEntity.ok(memberPageResponse);
    }

    @PostMapping("/{memberId}/following")
    @LoginRequired
    public ResponseEntity<Void> follow(@VerifiedMember final LoginPayload loginPayload,
                                       @PathVariable("memberId") final Long followeeId) {
        memberService.follow(loginPayload.getId(), followeeId);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/{memberId}/following")
    @LoginRequired
    public ResponseEntity<Void> unfollow(@VerifiedMember final LoginPayload loginPayload,
                                         @PathVariable("memberId") final Long followeeId) {
        memberService.unfollow(loginPayload.getId(), followeeId);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/me/followees")
    @LoginRequired
    public ResponseEntity<MemberPageResponse> searchFollowees(
            @VerifiedMember final LoginPayload loginPayload,
            @ModelAttribute final MemberSearchRequest memberSearchRequest,
            final Pageable pageable) {
        return ResponseEntity.ok(
                memberService.findFolloweesByConditions(loginPayload.getId(), memberSearchRequest, pageable));
    }

    private Long getNullableId(final LoginPayload loginPayload) {
        if (loginPayload == null) {
            return null;
        }
        return loginPayload.getId();
    }
}
