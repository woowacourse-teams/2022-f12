package com.woowacourse.f12.presentation.member;

import com.woowacourse.f12.application.auth.token.MemberPayload;
import com.woowacourse.f12.application.member.MemberService;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.response.member.LoggedInMemberResponse;
import com.woowacourse.f12.dto.response.member.MemberResponse;
import com.woowacourse.f12.presentation.auth.Login;
import com.woowacourse.f12.presentation.auth.VerifiedMember;
import com.woowacourse.f12.support.MemberPayloadSupport;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    public LoggedInMemberResponse showLoggedIn(@VerifiedMember final MemberPayload memberPayload) {
        return memberService.findLoggedInMember(memberPayload.getId());
    }

    @GetMapping("/{memberId}")
    @Login(required = false)
    public MemberResponse show(@PathVariable final Long memberId,
                               @VerifiedMember @Nullable final MemberPayload loggedInMemberPayload) {
        Long loggedInMemberId = MemberPayloadSupport.getLoggedInMemberId(loggedInMemberPayload);
        return memberService.find(memberId, loggedInMemberId);
    }

    @PatchMapping("/me")
    @Login
    public ResponseEntity<Void> updateMe(@VerifiedMember final MemberPayload memberPayload,
                                         @Valid @RequestBody final MemberRequest memberRequest) {
        memberService.updateMember(memberPayload.getId(), memberRequest);
        return ResponseEntity.ok().build();
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
}
