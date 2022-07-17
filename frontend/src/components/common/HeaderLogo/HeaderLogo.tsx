import * as S from './HeaderLogo.style';
import HeaderLogoImage from '@/assets/HeaderLogo.svg';
import ROUTES from '../../../constants/routes';

function HeaderLogo() {
  return (
    <S.Container to={ROUTES.HOME}>
      <HeaderLogoImage />
    </S.Container>
  );
}

export default HeaderLogo;
