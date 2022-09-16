package com.woowacourse.f12.application.auth;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import com.woowacourse.f12.exception.unauthorized.RefreshTokenExpiredException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

class RefreshTokenInfoTest {

    @Test
    void 리프레시_토큰_정보를_생성한다() {
        // given
        LocalDateTime expected = LocalDateTime.now().plusWeeks(2);

        // when
        RefreshTokenInfo refreshTokenInfo = RefreshTokenInfo.createByExpiredDay(1L, 14);
        LocalDateTime expiredAt = refreshTokenInfo.getExpiredAt();

        //then
        assertThat(expiredAt).isCloseTo(expected, within(1, ChronoUnit.SECONDS));
    }

    @Test
    void 리프레시_토큰_유효기간이_지난_경우_예외가_발생한다() {
        // given
        RefreshTokenInfo tokenInfo = RefreshTokenInfo.createByExpiredDay(1L, -1);

        // when, then
        assertThatThrownBy(tokenInfo::checkExpired)
                .isExactlyInstanceOf(RefreshTokenExpiredException.class);
    }
}
