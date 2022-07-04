import * as S from './Header.style';
import HeaderLogo from './HeaderLogo.svg';

function Header() {
  return (
    <S.Container>
      <S.LogoImageContainer to="/">
        <HeaderLogo />
      </S.LogoImageContainer>
    </S.Container>
  );
}

export default Header;
