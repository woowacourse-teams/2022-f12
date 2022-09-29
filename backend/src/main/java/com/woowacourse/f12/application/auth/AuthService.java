package com.woowacourse.f12.application.auth;

import com.woowacourse.f12.application.auth.token.JwtProvider;
import com.woowacourse.f12.application.auth.token.RefreshToken;
import com.woowacourse.f12.application.auth.token.RefreshTokenProvider;
import com.woowacourse.f12.application.auth.token.RefreshTokenRepository;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.dto.response.auth.GitHubProfileResponse;
import com.woowacourse.f12.dto.response.auth.IssuedTokensResponse;
import com.woowacourse.f12.dto.result.LoginResult;
import com.woowacourse.f12.exception.unauthorized.RefreshTokenExpiredException;
import com.woowacourse.f12.exception.unauthorized.RefreshTokenInvalidException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

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
    public LoginResult login(final String code) {
        final GitHubProfileResponse gitHubProfileResponse = getGitHubProfileResponse(code);
        final Member member = addOrUpdateMember(gitHubProfileResponse);
        final Long memberId = member.getId();
        final String applicationAccessToken = jwtProvider.createAccessToken(memberId);
        final RefreshToken refreshToken = refreshTokenProvider.createToken(memberId);
        refreshTokenRepository.save(refreshToken);
        return new LoginResult(refreshToken.getRefreshToken(), applicationAccessToken, member);
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

    public IssuedTokensResponse issueAccessToken(final String refreshTokenValue) {
        final RefreshToken refreshToken = refreshTokenRepository.findToken(refreshTokenValue)
                .orElseThrow(RefreshTokenInvalidException::new);
        checkExpired(refreshTokenValue, refreshToken);
        final Long memberId = refreshToken.getMemberId();
        final String newAccessToken = jwtProvider.createAccessToken(memberId);
        final RefreshToken newRefreshToken = refreshTokenProvider.createToken(memberId);
        refreshTokenRepository.save(newRefreshToken);
        refreshTokenRepository.delete(refreshTokenValue);
        return IssuedTokensResponse.of(newAccessToken, newRefreshToken);
    }

    private void checkExpired(final String refreshToken, final RefreshToken tokenInfo) {
        if (tokenInfo.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenExpiredException();
        }
    }
}
