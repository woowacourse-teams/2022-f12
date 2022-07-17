import {
  IsLoggedInContext,
  LogOutContext,
} from '@/contexts/LoginContextProvider';
import { useContext } from 'react';
import * as S from './HeaderNav.style';

function HeaderNav() {
  const isLoggedIn = useContext(IsLoggedInContext);
  const logout = useContext(LogOutContext);

  const handleLogout: React.MouseEventHandler<HTMLButtonElement> = () => {
    if (window.confirm('로그아웃 하시겠습니까?')) {
      try {
        logout();
        window.alert('로그아웃이 완료되었습니다.');
      } catch {
        window.alert('로그아웃에 실패했습니다. 다시 시도해주세요.');
      }
    }
  };

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
            <S.LogoutButton onClick={handleLogout}>로그아웃</S.LogoutButton>
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
