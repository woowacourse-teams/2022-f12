package com.woowacourse.f12.presentation;

import com.woowacourse.f12.application.MemberService;
import com.woowacourse.f12.dto.response.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<MemberResponse> showMe(@VerifiedMember final Long memberId) {
        final MemberResponse memberResponse = memberService.findById(memberId);
        return ResponseEntity.ok(memberResponse);
    }
}
