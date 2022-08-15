package com.woowacourse.f12.application.auth;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.dto.response.auth.GitHubProfileResponse;
import com.woowacourse.f12.dto.response.auth.LoginResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final GitHubOauthClient gitHubOauthClient;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public AuthService(final GitHubOauthClient gitHubOauthClient, final MemberRepository memberRepository,
                       final JwtProvider jwtProvider) {
        this.gitHubOauthClient = gitHubOauthClient;
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public LoginResponse login(final String code) {
        final String gitHubAccessToken = gitHubOauthClient.getAccessToken(code);
        final GitHubProfileResponse gitHubProfileResponse = gitHubOauthClient.getProfile(gitHubAccessToken);
        final Member requestedMember = gitHubProfileResponse.toMember();
        final Member member = memberRepository.findByGitHubId(gitHubProfileResponse.getGitHubId())
                .orElseGet(() -> memberRepository.save(requestedMember));
        member.update(requestedMember);
        final String applicationAccessToken = jwtProvider.createToken(member.getId());
        return LoginResponse.of(applicationAccessToken, member);
    }
}
