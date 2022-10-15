import * as S from '@/components/common/BottomNavigation/BottomNavigation.style';

import useAuth from '@/hooks/useAuth';

import { GITHUB_AUTH_URL } from '@/constants/api';
import ROUTES from '@/constants/routes';

import Logo from '@/assets/logo.svg';

function BottomNavigation() {
  const { logout, isLoggedIn } = useAuth();

  return (
    <S.Container>
      <S.NavButton to={ROUTES.HOME}>
        <Logo />홈
      </S.NavButton>
      <S.NavButton to={ROUTES.PRODUCTS}>
        <Logo />
        제품 검색
      </S.NavButton>
      <S.NavButton to={ROUTES.PROFILE_SEARCH}>
        <Logo />
        프로필 검색
      </S.NavButton>
      <S.NavButton to={ROUTES.MY_PROFILE}>
        <Logo />내 프로필
      </S.NavButton>
      {isLoggedIn ? (
        <S.LogoutLink onClick={logout}>
          <Logo />
          로그아웃
        </S.LogoutLink>
      ) : (
        <S.LoginLink href={GITHUB_AUTH_URL}>
          <Logo />
          로그인
        </S.LoginLink>
      )}
    </S.Container>
  );
}

export default BottomNavigation;
