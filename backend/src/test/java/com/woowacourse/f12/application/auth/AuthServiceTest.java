package com.woowacourse.f12.application.auth;

import static com.woowacourse.f12.support.MemberFixtures.CORINNE;
import static com.woowacourse.f12.support.MemberFixtures.CORINNE_UPDATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.dto.response.auth.GitHubProfileResponse;
import com.woowacourse.f12.dto.response.auth.TokenResponse;
import com.woowacourse.f12.dto.response.member.MemberResponse;
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

    @Test
    void 깃허브_코드가_들어왔을때_회원정보가_없으면_새로_저장하고_회원정보와_토큰을_반환한다() {
        // given
        String code = "abcde";
        String accessToken = "accessToken";
        String expectedAccessToken = "applicationToken";
        String refreshToken = "refreshToken";
        GitHubProfileResponse gitHubProfile = CORINNE.깃허브_프로필();
        Member member = CORINNE.생성(1L);

        given(gitHubOauthClient.getAccessToken(code))
                .willReturn(accessToken);
        given(gitHubOauthClient.getProfile(accessToken))
                .willReturn(gitHubProfile);
        given(memberRepository.findByGitHubId(gitHubProfile.getGitHubId()))
                .willReturn(Optional.empty());
        given(memberRepository.save(gitHubProfile.toMember()))
                .willReturn(member);
        given(jwtProvider.createToken(member.getId()))
                .willReturn(expectedAccessToken);
        given(refreshTokenProvider.createToken())
                .willReturn(refreshToken);

        // when
        TokenResponse tokenResponse = authService.login2(code);

        // then
        assertAll(
                () -> assertThat(tokenResponse.getAccessToken()).isEqualTo(expectedAccessToken),
                () -> assertThat(tokenResponse.getLoginResponse().getMember()).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.of(member, false)),
                () -> assertThat(tokenResponse.getRefreshToken()).isEqualTo(refreshToken),
                () -> verify(gitHubOauthClient).getAccessToken(code),
                () -> verify(gitHubOauthClient).getProfile(accessToken),
                () -> verify(memberRepository).findByGitHubId(gitHubProfile.getGitHubId()),
                () -> verify(memberRepository).save(gitHubProfile.toMember()),
                () -> verify(jwtProvider).createToken(1L),
                () -> verify(refreshTokenProvider).createToken()
        );
    }

    @Test
    void 깃허브_코드가_들어왔을때_회원정보가_있으면_이름을_업데이트하고_회원정보와_토큰을_반환한다() {
        // given
        String code = "abcde";
        String accessToken = "accessToken";
        String applicationToken = "applicationToken";
        String refreshToken = "refreshToken";

        GitHubProfileResponse gitHubProfile = CORINNE_UPDATED.깃허브_프로필();
        Member member = CORINNE.생성(1L);
        given(gitHubOauthClient.getAccessToken(code))
                .willReturn(accessToken);
        given(gitHubOauthClient.getProfile(accessToken))
                .willReturn(gitHubProfile);
        given(memberRepository.findByGitHubId(gitHubProfile.getGitHubId()))
                .willReturn(Optional.of(member));
        given(jwtProvider.createToken(member.getId()))
                .willReturn(applicationToken);
        given(refreshTokenProvider.createToken())
                .willReturn(refreshToken);

        // when
        TokenResponse tokenResponse = authService.login2(code);

        // then
        assertAll(
                () -> assertThat(tokenResponse.getAccessToken()).isEqualTo(applicationToken),
                () -> assertThat(tokenResponse.getLoginResponse().getMember()).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.of(member, false)),
                () -> verify(gitHubOauthClient).getAccessToken(code),
                () -> verify(gitHubOauthClient).getProfile(accessToken),
                () -> verify(memberRepository).findByGitHubId(gitHubProfile.getGitHubId()),
                () -> verify(jwtProvider).createToken(1L),
                () -> verify(refreshTokenProvider).createToken()
        );
    }
}
