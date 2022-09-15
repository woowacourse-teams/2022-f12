package com.woowacourse.f12.application.auth;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.dto.response.auth.GitHubProfileResponse;
import com.woowacourse.f12.dto.response.auth.LoginResponse;
import com.woowacourse.f12.dto.response.auth.TokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final GitHubOauthClient gitHubOauthClient;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenProvider refreshTokenProvider;

    public AuthService(final GitHubOauthClient gitHubOauthClient, final MemberRepository memberRepository,
                       final JwtProvider jwtProvider, final RefreshTokenProvider refreshTokenProvider) {
        this.gitHubOauthClient = gitHubOauthClient;
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
        this.refreshTokenProvider = refreshTokenProvider;
    }

    @Transactional
    public LoginResponse login(final String code) {
        final GitHubProfileResponse gitHubProfileResponse = getGitHubProfileResponse(code);
        final Member member = addOrUpdateMember(gitHubProfileResponse);
        final String applicationAccessToken = jwtProvider.createToken(member.getId());
        return LoginResponse.of(applicationAccessToken, member);
    }

    @Transactional
    public TokenResponse login2(final String code) {
        final GitHubProfileResponse gitHubProfileResponse = getGitHubProfileResponse(code);
        final Member member = addOrUpdateMember(gitHubProfileResponse);
        final String applicationAccessToken = jwtProvider.createToken(member.getId());
        final LoginResponse loginResponse = LoginResponse.of(applicationAccessToken, member);
        final String refreshToken = refreshTokenProvider.createToken();
        return new TokenResponse(refreshToken, loginResponse);
    }

    private GitHubProfileResponse getGitHubProfileResponse(final String code) {
        final String gitHubAccessToken = gitHubOauthClient.getAccessToken(code);
        return gitHubOauthClient.getProfile(gitHubAccessToken);
    }

    private Member addOrUpdateMember(final GitHubProfileResponse gitHubProfileResponse) {
        final Member requestedMember = gitHubProfileResponse.toMember();
        final Member member = memberRepository.findByGitHubId(gitHubProfileResponse.getGitHubId())
                .orElseGet(() -> memberRepository.save(requestedMember));
        member.update(requestedMember);
        return member;
    }
}
