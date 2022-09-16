package com.woowacourse.f12.application.auth;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.dto.response.auth.GitHubProfileResponse;
import com.woowacourse.f12.dto.response.auth.IssuedTokensResponse;
import com.woowacourse.f12.dto.response.auth.LoginResponse;
import com.woowacourse.f12.dto.response.auth.TokenResponse;
import com.woowacourse.f12.exception.unauthorized.RefreshTokenNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private static final int REFRESH_TOKEN_EXPIRED_DAYS = 14;
    private final GitHubOauthClient gitHubOauthClient;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(final GitHubOauthClient gitHubOauthClient, final MemberRepository memberRepository,
                       final JwtProvider jwtProvider, final RefreshTokenProvider refreshTokenProvider,
                       final RefreshTokenRepository refreshTokenRepository) {
        this.gitHubOauthClient = gitHubOauthClient;
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public TokenResponse login(final String code) {
        final GitHubProfileResponse gitHubProfileResponse = getGitHubProfileResponse(code);
        final Member member = addOrUpdateMember(gitHubProfileResponse);
        final String applicationAccessToken = jwtProvider.createToken(member.getId());
        final LoginResponse loginResponse = LoginResponse.of(applicationAccessToken, member);
        final String refreshToken = refreshTokenProvider.createToken();
        refreshTokenRepository.save(refreshToken, RefreshTokenInfo.createByExpiredDay(member.getId(), 14));
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

    public IssuedTokensResponse issueAccessToken(final String refreshToken) {
        final RefreshTokenInfo tokenInfo = refreshTokenRepository.findTokenInfo(refreshToken)
                .orElseThrow(RefreshTokenNotFoundException::new);
        tokenInfo.checkExpired();
        final Long memberId = tokenInfo.getMemberId();
        final String newAccessToken = jwtProvider.createToken(memberId);
        final String newRefreshToken = refreshTokenProvider.createToken();
        final RefreshTokenInfo newTokenInfo = RefreshTokenInfo.createByExpiredDay(memberId, REFRESH_TOKEN_EXPIRED_DAYS);
        refreshTokenRepository.save(newRefreshToken, newTokenInfo);
        return new IssuedTokensResponse(newAccessToken, newRefreshToken);
    }
}
