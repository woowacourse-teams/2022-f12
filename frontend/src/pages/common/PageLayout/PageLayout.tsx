import { Outlet, useLocation } from 'react-router-dom';

import * as S from '@/pages/common/PageLayout/PageLayout.style';

import HeaderLogo from '@/components/common/HeaderLogo/HeaderLogo';
import HeaderNav from '@/components/common/HeaderNav/HeaderNav';
import { useEffect } from 'react';

function PageLayout() {
  const location = useLocation();

  useEffect(() => {
    window.scrollTo({
      top: 0,
      left: 0,
    });
  }, [location.key]);

  return (
    <>
      <HeaderLogo />
      <HeaderNav />
      <S.Main>
        <Outlet />
      </S.Main>
    </>
  );
}

export default PageLayout;
