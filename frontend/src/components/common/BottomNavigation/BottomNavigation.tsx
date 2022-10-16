import * as S from '@/components/common/BottomNavigation/BottomNavigation.style';

import ROUTES from '@/constants/routes';

function BottomNavigation() {
  return (
    <S.Container aria-label={'하단 메뉴바'}>
      <S.NavList>
        <li>
          <S.NavButton to={ROUTES.PRODUCTS}>제품 검색</S.NavButton>
        </li>
        <li>
          <S.NavButton to={ROUTES.HOME}>홈</S.NavButton>
        </li>
        <li>
          <S.NavButton to={ROUTES.PROFILE_SEARCH}>프로필 검색</S.NavButton>
        </li>
      </S.NavList>
    </S.Container>
  );
}

export default BottomNavigation;
