package com.woowacourse.f12.presentation;

import com.woowacourse.f12.application.MemberService;
import com.woowacourse.f12.dto.request.ProfileProductRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @PatchMapping("/inventoryProducts")
    @LoginRequired
    public ResponseEntity<Void> updateProfileProducts(@RequestBody final ProfileProductRequest profileProductRequest,
                                                      @VerifiedMember final Long memberId) {
        memberService.updateProfileProducts(memberId, profileProductRequest);
        return ResponseEntity.ok().build();
    }
}
