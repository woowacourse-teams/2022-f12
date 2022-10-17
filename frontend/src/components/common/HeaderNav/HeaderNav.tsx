import { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';

import CategoryNav from '@/components/common/CategoryNav/CategoryNav';
import * as S from '@/components/common/HeaderNav/HeaderNav.style';

import useAnimation from '@/hooks/useAnimation';
import useAuth from '@/hooks/useAuth';

import { GITHUB_AUTH_URL } from '@/constants/api';
import ROUTES from '@/constants/routes';

import { CustomNavLink } from '@/style/GlobalStyles';

import HeaderLogoImageHorizontal from '@/assets/HeaderLogoHorizontal.svg';
import ProfileImage from '@/assets/profile.svg';

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
    <S.Nav aria-label={'상단 메뉴바'}>
      <S.Wrapper>
        <S.FlexLeftWrapper>
          <S.TransparentButton onClick={handleCategoryToggle} aria-label="카테고리">
            카테고리
          </S.TransparentButton>
          <CustomNavLink to={ROUTES.PRODUCTS}>제품 검색</CustomNavLink>
          <CustomNavLink to={ROUTES.PROFILE_SEARCH}>프로필 검색</CustomNavLink>
        </S.FlexLeftWrapper>
        <S.FlexRightWrapper>
          {isLoggedIn ? (
            <>
              <CustomNavLink to={ROUTES.MY_PROFILE}>내 프로필</CustomNavLink>
              <CustomNavLink to={ROUTES.FOLLOWING}>팔로잉</CustomNavLink>
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

function Mobile() {
  const { isLoggedIn } = useAuth();
  return (
    <S.Nav aria-label={'상단 메뉴바'}>
      <S.LogoLink to={ROUTES.HOME}>
        <HeaderLogoImageHorizontal />
      </S.LogoLink>
      {isLoggedIn ? (
        <S.CustomLink to={ROUTES.MY_PROFILE} aria-label={'내 프로필 보기'}>
          <ProfileImage />
        </S.CustomLink>
      ) : (
        <S.CustomLink as={'a'} href={GITHUB_AUTH_URL}>
          로그인
        </S.CustomLink>
      )}
    </S.Nav>
  );
}

HeaderNav.Mobile = Mobile;

export default HeaderNav;
