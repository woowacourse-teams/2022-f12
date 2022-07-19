import { GITHUB_AUTH_URL } from '@/constants/api';
import useAuth from '@/hooks/useAuth';

import * as S from './HeaderNav.style';

function HeaderNav() {
  const { logout, isLoggedIn } = useAuth();

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
            <S.LoginButton href={GITHUB_AUTH_URL}>로그인</S.LoginButton>
          )}
        </S.FlexRightUl>
      </S.Wrapper>
    </S.Nav>
  );
}

export default HeaderNav;
