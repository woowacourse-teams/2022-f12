import { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';

import CategoryNav from '@/components/common/CategoryNav/CategoryNav';
import * as S from '@/components/common/HeaderNav/HeaderNav.style';

import useAnimation from '@/hooks/useAnimation';
import useAuth from '@/hooks/useAuth';
import useModal from '@/hooks/useModal';

import { GITHUB_AUTH_URL } from '@/constants/api';
import ROUTES from '@/constants/routes';

function HeaderNav() {
  const { logout, isLoggedIn } = useAuth();
  const { showAlert, getConfirm } = useModal();

  const [categoryOpen, setCategoryOpen] = useState(false);
  const [shouldRenderCategory, handleTransitionEnd, triggerAnimation] =
    useAnimation(categoryOpen);

  const location = useLocation();

  const handleCategoryToggle = () => {
    setCategoryOpen((prevState) => !prevState);
  };

  const handleLogout = async () => {
    const confirmation = await getConfirm('로그아웃 하시겠습니까?');
    if (confirmation) {
      try {
        logout();
        showAlert('로그아웃이 완료되었습니다.');
      } catch {
        showAlert('로그아웃에 실패했습니다. 다시 시도해주세요.');
      }
    }
  };

  useEffect(() => {
    setCategoryOpen(false);
  }, [location.key]);

  return (
    <S.Nav>
      <S.Wrapper>
        <S.FlexLeftUl>
          <S.TransparentButton
            onClick={handleCategoryToggle}
            aria-label="카테고리"
          >
            카테고리
          </S.TransparentButton>
          <Link to={ROUTES.PRODUCTS}>제품 검색</Link>
          <Link to={ROUTES.PROFILE_SEARCH}>프로필 검색</Link>
        </S.FlexLeftUl>
        <S.FlexRightUl>
          {isLoggedIn ? (
            <>
              <S.ProfileLink to={ROUTES.PROFILE}>내 프로필</S.ProfileLink>
              <S.TransparentButton onClick={handleLogout}>
                로그아웃
              </S.TransparentButton>
            </>
          ) : (
            <S.LoginButton href={GITHUB_AUTH_URL}>로그인</S.LoginButton>
          )}
        </S.FlexRightUl>
      </S.Wrapper>
      {shouldRenderCategory && (
        <CategoryNav
          handleTransitionEnd={handleTransitionEnd}
          triggerAnimation={triggerAnimation}
        />
      )}
    </S.Nav>
  );
}

export default HeaderNav;
