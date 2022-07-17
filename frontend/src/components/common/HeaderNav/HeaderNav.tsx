import { IsLoggedInContext } from '@/contexts/LoginContextProvider';
import { useContext } from 'react';
import * as S from './HeaderNav.style';

function HeaderNav() {
  const isLoggedIn = useContext(IsLoggedInContext);

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
          {isLoggedIn ? (
            <p>로그아웃</p>
          ) : (
            <S.LoginButton
              href={`https://github.com/login/oauth/authorize?client_id=f1e73a9ac502f1b6712a&redirect_uri=http://localhost:3000/login`}
            >
              로그인
            </S.LoginButton>
          )}
        </S.FlexRightUl>
      </S.Wrapper>
    </S.Nav>
  );
}

export default HeaderNav;
