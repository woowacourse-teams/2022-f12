package com.woowacourse.f12.application;

import com.woowacourse.f12.domain.Member;
import com.woowacourse.f12.domain.MemberRepository;
import com.woowacourse.f12.dto.response.GitHubProfileResponse;
import com.woowacourse.f12.dto.response.LoginResponse;
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
        final Member member = memberRepository.findByGitHubId(gitHubProfileResponse.getGitHubId())
                .orElseGet(() -> memberRepository.save(gitHubProfileResponse.toMember()));
        member.updateName(gitHubProfileResponse.getName());
        final String applicationAccessToken = jwtProvider.createToken(member.getId());
        return LoginResponse.of(applicationAccessToken, member);
    }
}
