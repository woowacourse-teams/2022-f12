import * as S from '@/components/common/HeaderLogo/HeaderLogo.style';

import ROUTES from '@/constants/routes';

import HeaderLogoImage from '@/assets/HeaderLogo.svg';

function HeaderLogo() {
  return (
    <S.Container>
      <S.LogoLink to={ROUTES.HOME} aria-label={'F12 홈으로'}>
        <HeaderLogoImage />
      </S.LogoLink>
    </S.Container>
  );
}

export default HeaderLogo;
