import * as S from '@/components/common/HeaderLogo/HeaderLogo.style';

import ROUTES from '@/constants/routes';

import HeaderLogoImage from '@/assets/HeaderLogo.svg';

function HeaderLogo() {
  return (
    <S.Container to={ROUTES.HOME}>
      <HeaderLogoImage />
    </S.Container>
  );
}

export default HeaderLogo;
