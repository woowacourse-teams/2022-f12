import { GITHUB_AUTH_URL } from '@/constants/api';
import ROUTES from '@/constants/routes';
import useAuth from '@/hooks/useAuth';
import { Link } from 'react-router-dom';

import * as S from '@/components/common/HeaderNav/HeaderNav.style';

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
          <Link to={ROUTES.PRODUCTS}>전체 상품 목록</Link>
          <Link to={{ pathname: ROUTES.PRODUCTS, hash: '#popular' }}>
            인기 상품 목록
          </Link>
        </S.FlexLeftUl>
        <S.FlexRightUl>
          {isLoggedIn ? (
            <>
              <S.ProfileLink to={ROUTES.PROFILE}>내 프로필</S.ProfileLink>
              <S.LogoutButton onClick={handleLogout}>로그아웃</S.LogoutButton>
            </>
          ) : (
            <S.LoginButton href={GITHUB_AUTH_URL}>로그인</S.LoginButton>
          )}
        </S.FlexRightUl>
      </S.Wrapper>
    </S.Nav>
  );
}

export default HeaderNav;
