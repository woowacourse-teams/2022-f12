package com.woowacourse.f12.presentation;

import com.woowacourse.f12.application.MemberService;
import com.woowacourse.f12.dto.request.MemberRequest;
import com.woowacourse.f12.dto.response.MemberResponse;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<MemberResponse> showLoggedIn(@VerifiedMember final Long memberId) {
        final MemberResponse memberResponse = memberService.findById(memberId);
        return ResponseEntity.ok(memberResponse);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> show(@PathVariable final Long memberId) {
        final MemberResponse memberResponse = memberService.findById(memberId);
        return ResponseEntity.ok(memberResponse);
    }

    @PatchMapping("/me")
    @LoginRequired
    public ResponseEntity<Void> updateMe(@VerifiedMember final Long memberId,
                                         @Valid @RequestBody final MemberRequest memberRequest) {
        memberService.updateMember(memberId, memberRequest);
        return ResponseEntity.ok().build();
    }
}
