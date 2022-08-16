import { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';

import CategoryNav from '@/components/common/CategoryNav/CategoryNav';
import * as S from '@/components/common/HeaderNav/HeaderNav.style';

import useAnimation from '@/hooks/useAnimation';
import useAuth from '@/hooks/useAuth';

import { GITHUB_AUTH_URL } from '@/constants/api';
import ROUTES from '@/constants/routes';

function HeaderNav() {
  const [categoryOpen, setCategoryOpen] = useState(false);
  const location = useLocation();

  const { logout, isLoggedIn } = useAuth();

  const [shouldRenderCategory, handleTransitionEnd, triggerAnimation] =
    useAnimation(categoryOpen);

  const handleCategoryToggle = () => {
    setCategoryOpen((prevState) => !prevState);
  };

  useEffect(() => {
    setCategoryOpen(false);
  }, [location.key]);

  return (
    <S.Nav>
      <S.Wrapper>
        <S.FlexLeftWrapper>
          <S.TransparentButton onClick={handleCategoryToggle} aria-label="카테고리">
            카테고리
          </S.TransparentButton>
          <Link to={ROUTES.PRODUCTS}>제품 검색</Link>
          <Link to={ROUTES.PROFILE_SEARCH}>프로필 검색</Link>
        </S.FlexLeftWrapper>
        <S.FlexRightWrapper>
          {isLoggedIn ? (
            <>
              <S.ProfileLink to={ROUTES.PROFILE}>내 프로필</S.ProfileLink>
              <S.TransparentButton onClick={logout}>로그아웃</S.TransparentButton>
            </>
          ) : (
            <S.LoginLink href={GITHUB_AUTH_URL}>로그인</S.LoginLink>
          )}
        </S.FlexRightWrapper>
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
