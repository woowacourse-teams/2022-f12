import { Link } from 'react-router-dom';
import * as S from './HeaderNav.style';
function HeaderNav() {
  return (
    <S.Nav>
      <S.Wrapper>
        <S.FlexLeftUl>
          <li>메뉴1</li>
          <li>메뉴2</li>
          <li>메뉴3</li>
          <li>메뉴4</li>
        </S.FlexLeftUl>
        <S.FlexRightUl>
          <Link to="/">로그인</Link>
        </S.FlexRightUl>
      </S.Wrapper>
    </S.Nav>
  );
}

export default HeaderNav;
