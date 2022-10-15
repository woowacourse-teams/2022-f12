import * as S from '@/components/common/HeaderLogo/HeaderLogo.style';

import useAuth from '@/hooks/useAuth';

import { GITHUB_AUTH_URL } from '@/constants/api';
import ROUTES from '@/constants/routes';

import HeaderLogoImage from '@/assets/HeaderLogo.svg';
import HeaderLogoImageHorizontal from '@/assets/HeaderLogoHorizontal.svg';
import Profile from '@/assets/profile.svg';

function HeaderLogo() {
  return (
    <S.Container>
      <S.LogoLink to={ROUTES.HOME}>
        <HeaderLogoImage />
      </S.LogoLink>
    </S.Container>
  );
}

function Mobile() {
  const { isLoggedIn } = useAuth();
  return (
    <S.Container>
      <S.LogoLink to={ROUTES.HOME}>
        <HeaderLogoImageHorizontal />
      </S.LogoLink>
      {isLoggedIn ? (
        <S.CustomLink to={ROUTES.MY_PROFILE}>
          <Profile />
        </S.CustomLink>
      ) : (
        <S.CustomLink as={'a'} href={GITHUB_AUTH_URL}>
          로그인
        </S.CustomLink>
      )}
    </S.Container>
  );
}

HeaderLogo.Mobile = Mobile;

export default HeaderLogo;
