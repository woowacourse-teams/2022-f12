package com.woowacourse.f12.application.auth;

import static com.woowacourse.f12.support.fixture.MemberFixture.CORINNE;
import static com.woowacourse.f12.support.fixture.MemberFixture.CORINNE_UPDATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.dto.response.auth.GitHubProfileResponse;
import com.woowacourse.f12.dto.response.auth.IssuedTokensResponse;
import com.woowacourse.f12.dto.result.LoginResult;
import com.woowacourse.f12.exception.unauthorized.RefreshTokenExpiredException;
import com.woowacourse.f12.exception.unauthorized.RefreshTokenInvalidException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private GitHubOauthClient gitHubOauthClient;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private RefreshTokenProvider refreshTokenProvider;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private LocalDateTime expiredAt = LocalDateTime.now().plusDays(14);
    private Long memberId = 1L;

    @Test
    void 깃허브_코드가_들어왔을때_회원정보가_없으면_새로_저장하고_회원정보와_토큰을_반환한다() {
        // given
        String code = "abcde";
        String accessToken = "accessToken";
        String expectedAccessToken = "applicationToken";
        String refreshTokenValue = "refreshToken";
        GitHubProfileResponse gitHubProfile = CORINNE.깃허브_프로필();
        Member member = CORINNE.생성(memberId);

        given(gitHubOauthClient.getAccessToken(code))
                .willReturn(accessToken);
        given(gitHubOauthClient.getProfile(accessToken))
                .willReturn(gitHubProfile);
        given(memberRepository.findByGitHubId(gitHubProfile.getGitHubId()))
                .willReturn(Optional.empty());
        given(memberRepository.save(gitHubProfile.toMember()))
                .willReturn(member);
        given(jwtProvider.createAccessToken(member.getId()))
                .willReturn(expectedAccessToken);
        given(refreshTokenProvider.createToken(memberId))
                .willReturn(new RefreshToken(refreshTokenValue, memberId, expiredAt));

        // when
        LoginResult loginResult = authService.login(code);

        // then
        assertAll(
                () -> assertThat(loginResult.getAccessToken()).isEqualTo(expectedAccessToken),
                () -> assertThat(loginResult.getMember()).usingRecursiveComparison()
                        .isEqualTo(member),
                () -> assertThat(loginResult.getRefreshToken()).isEqualTo(refreshTokenValue),
                () -> verify(gitHubOauthClient).getAccessToken(code),
                () -> verify(gitHubOauthClient).getProfile(accessToken),
                () -> verify(memberRepository).findByGitHubId(gitHubProfile.getGitHubId()),
                () -> verify(memberRepository).save(gitHubProfile.toMember()),
                () -> verify(jwtProvider).createAccessToken(memberId),
                () -> verify(refreshTokenProvider).createToken(memberId)
        );
    }

    @Test
    void 깃허브_코드가_들어왔을때_회원정보가_있으면_이름을_업데이트하고_회원정보와_토큰을_반환한다() {
        // given
        String code = "abcde";
        String accessToken = "accessToken";
        String applicationToken = "applicationToken";
        String refreshTokenValue = "refreshToken";

        GitHubProfileResponse gitHubProfile = CORINNE_UPDATED.깃허브_프로필();
        Member member = CORINNE.생성(memberId);
        given(gitHubOauthClient.getAccessToken(code))
                .willReturn(accessToken);
        given(gitHubOauthClient.getProfile(accessToken))
                .willReturn(gitHubProfile);
        given(memberRepository.findByGitHubId(gitHubProfile.getGitHubId()))
                .willReturn(Optional.of(member));
        given(jwtProvider.createAccessToken(member.getId()))
                .willReturn(applicationToken);
        given(refreshTokenProvider.createToken(memberId))
                .willReturn(new RefreshToken(refreshTokenValue, memberId, expiredAt));

        // when
        LoginResult loginResult = authService.login(code);

        // then
        assertAll(
                () -> assertThat(loginResult.getAccessToken()).isEqualTo(applicationToken),
                () -> assertThat(loginResult.getMember()).usingRecursiveComparison()
                        .isEqualTo(member),
                () -> verify(gitHubOauthClient).getAccessToken(code),
                () -> verify(gitHubOauthClient).getProfile(accessToken),
                () -> verify(memberRepository).findByGitHubId(gitHubProfile.getGitHubId()),
                () -> verify(jwtProvider).createAccessToken(memberId),
                () -> verify(refreshTokenProvider).createToken(memberId)
        );
    }

    @Test
    void 유효한_리프레시_토큰으로_액세스_토큰을_발급한다() {
        // given
        String refreshTokenValue = "validRefreshToken";
        String newAccessTokenValue = "newAccessToken";
        String newRefreshTokenValue = "newRefreshToken";
        RefreshToken refreshToken = new RefreshToken(refreshTokenValue, memberId, expiredAt);
        RefreshToken newRefreshToken = new RefreshToken(newRefreshTokenValue, memberId, expiredAt);

        given(refreshTokenRepository.findToken(refreshTokenValue))
                .willReturn(Optional.of(refreshToken));
        given(refreshTokenProvider.createToken(memberId))
                .willReturn(newRefreshToken);
        given(refreshTokenRepository.save(eq(newRefreshToken)))
                .willReturn(newRefreshToken);
        given(jwtProvider.createAccessToken(1L))
                .willReturn(newAccessTokenValue);
        willDoNothing().given(refreshTokenRepository).delete(refreshTokenValue);

        // when
        IssuedTokensResponse expect = authService.issueAccessToken(refreshTokenValue);

        // then
        assertAll(
                () -> assertThat(expect.getAccessToken()).isEqualTo(newAccessTokenValue),
                () -> assertThat(expect.getRefreshToken()).isEqualTo(newRefreshTokenValue),
                () -> verify(refreshTokenRepository).findToken(refreshTokenValue),
                () -> verify(refreshTokenProvider).createToken(memberId),
                () -> verify(refreshTokenRepository).save(eq(newRefreshToken)),
                () -> verify(jwtProvider).createAccessToken(1L),
                () -> verify(refreshTokenRepository).delete(refreshTokenValue)
        );
    }

    @Test
    void 저장되어_있지않은_리프레시_토큰으로_액세스_토큰_발급하려할_경우_예외_발생() {
        // given
        given(refreshTokenRepository.findToken(any()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> authService.issueAccessToken("refreshToken"))
                .isExactlyInstanceOf(RefreshTokenInvalidException.class);
    }

    @Test
    void 만료된_리프레시_토큰으로_액세스_토큰_발급하려할_경우_예외_발생() {
        // given
        String refreshTokenValue = "refreshToken";
        LocalDateTime expiredDate = LocalDateTime.now().minusDays(1);
        given(refreshTokenRepository.findToken(any()))
                .willReturn(Optional.of(new RefreshToken(refreshTokenValue, memberId, expiredDate)));
        willDoNothing().given(refreshTokenRepository).delete(refreshTokenValue);

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> authService.issueAccessToken(refreshTokenValue))
                        .isExactlyInstanceOf(RefreshTokenExpiredException.class),
                () -> verify(refreshTokenRepository).delete(refreshTokenValue),
                () -> verify(refreshTokenRepository).findToken(any())
        );
    }
}
