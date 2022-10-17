import { Suspense, useEffect, useState } from 'react';
import { Outlet, useLocation } from 'react-router-dom';

import * as S from '@/pages/common/PageLayout/PageLayout.style';

import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import BottomNavigation from '@/components/common/BottomNavigation/BottomNavigation';
import HeaderLogo from '@/components/common/HeaderLogo/HeaderLogo';
import HeaderNav from '@/components/common/HeaderNav/HeaderNav';
import Loading from '@/components/common/Loading/Loading';

import useAuth from '@/hooks/useAuth';
import useDevice from '@/hooks/useDevice';

function PageLayout() {
  const location = useLocation();

  useEffect(() => {
    window.scrollTo({
      top: 0,
      left: 0,
    });
  }, [location.key]);

  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isReady, setIsReady] = useState(false);
  const { revalidate } = useAuth();

  useEffect(() => {
    revalidate()
      .catch(() => {
        console.log('error');
      })
      .finally(() => {
        setIsAuthenticated(true);
      });
  }, []);

  useEffect(() => {
    if (isAuthenticated === true) {
      setIsReady(true);
      return;
    }
  }, [isAuthenticated]);

  const { device } = useDevice();

  return (
    <>
      {device !== 'desktop' && <HeaderNav.Mobile />}

      {device === 'desktop' && (
        <>
          <HeaderLogo />
          <HeaderNav />
        </>
      )}
      <Suspense>
        <S.Main>
          <AsyncWrapper isReady={isReady} fallback={<Loading />}>
            <Outlet />
          </AsyncWrapper>
        </S.Main>
      </Suspense>
      {device !== 'desktop' && <BottomNavigation />}
    </>
  );
}

export default PageLayout;
