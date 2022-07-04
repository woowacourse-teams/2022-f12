import * as S from './Header.style';
import HeaderLogo from './HeaderLogo.svg';
import ROUTES from '../../../constants/routes';

function Header() {
  return (
    <S.Container>
      <S.LogoImageContainer to={ROUTES.HOME}>
        <HeaderLogo />
      </S.LogoImageContainer>
    </S.Container>
  );
}

export default Header;
