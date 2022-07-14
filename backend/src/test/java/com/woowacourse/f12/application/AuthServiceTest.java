package com.woowacourse.f12.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.woowacourse.f12.domain.Member;
import com.woowacourse.f12.domain.MemberRepository;
import com.woowacourse.f12.dto.response.GitHubProfileResponse;
import com.woowacourse.f12.dto.response.LoginResponse;
import com.woowacourse.f12.dto.response.MemberResponse;
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

    @Test
    void 깃허브_코드가_들어왔을때_회원정보가_없으면_새로_저장하고_회원정보와_토큰을_반환한다() {
        // given
        String code = "abcde";
        String accessToken = "accessToken";
        String applicationToken = "applicationToken";
        GitHubProfileResponse gitHubProfileResponse = new GitHubProfileResponse("gitHubId", "name", "url");
        Member member = Member.builder()
                .id(1L)
                .gitHubId("gitHubId")
                .name("name")
                .imageUrl("url")
                .build();
        given(gitHubOauthClient.getAccessToken(code))
                .willReturn(accessToken);
        given(gitHubOauthClient.getProfile(accessToken))
                .willReturn(gitHubProfileResponse);
        given(memberRepository.findByGitHubId("gitHubId"))
                .willReturn(Optional.empty());
        given(memberRepository.save(gitHubProfileResponse.toMember()))
                .willReturn(member);
        given(jwtProvider.createToken(member.getId()))
                .willReturn(applicationToken);

        // when
        LoginResponse loginResponse = authService.login(code);

        // then
        assertAll(
                () -> assertThat(loginResponse.getToken()).isEqualTo(applicationToken),
                () -> assertThat(loginResponse.getMember()).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.from(member)),
                () -> verify(gitHubOauthClient).getAccessToken(code),
                () -> verify(gitHubOauthClient).getProfile(accessToken),
                () -> verify(memberRepository).findByGitHubId("gitHubId"),
                () -> verify(memberRepository).save(gitHubProfileResponse.toMember()),
                () -> verify(jwtProvider).createToken(1L)
        );
    }

    @Test
    void 깃허브_코드가_들어왔을때_회원정보가_있으면_이름을_업데이트하고_회원정보와_토큰을_반환한다() {
        // given
        String code = "abcde";
        String accessToken = "accessToken";
        String applicationToken = "applicationToken";
        GitHubProfileResponse gitHubProfileResponse = new GitHubProfileResponse("gitHubId", "updateName", "url");
        Member member = Member.builder()
                .id(1L)
                .gitHubId("gitHubId")
                .name("name")
                .imageUrl("url")
                .build();
        given(gitHubOauthClient.getAccessToken(code))
                .willReturn(accessToken);
        given(gitHubOauthClient.getProfile(accessToken))
                .willReturn(gitHubProfileResponse);
        given(memberRepository.findByGitHubId("gitHubId"))
                .willReturn(Optional.of(member));
        given(jwtProvider.createToken(member.getId()))
                .willReturn(applicationToken);

        // when
        LoginResponse loginResponse = authService.login(code);

        // then
        assertAll(
                () -> assertThat(loginResponse.getToken()).isEqualTo(applicationToken),
                () -> assertThat(loginResponse.getMember()).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.from(member)),
                () -> verify(gitHubOauthClient).getAccessToken(code),
                () -> verify(gitHubOauthClient).getProfile(accessToken),
                () -> verify(memberRepository).findByGitHubId("gitHubId"),
                () -> verify(jwtProvider).createToken(1L)
        );
    }
}
