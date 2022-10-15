import * as S from '@/components/common/BottomNavigation/BottomNavigation.style';

import ROUTES from '@/constants/routes';

function BottomNavigation() {
  return (
    <S.Container>
      <S.NavButton to={ROUTES.PRODUCTS}>제품 검색</S.NavButton>
      <S.NavButton to={ROUTES.HOME}>홈</S.NavButton>
      <S.NavButton to={ROUTES.PROFILE_SEARCH}>프로필 검색</S.NavButton>
    </S.Container>
  );
}

export default BottomNavigation;
