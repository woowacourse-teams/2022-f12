import { Suspense, useEffect, useLayoutEffect, useRef, useState } from 'react';
import { Outlet, useLocation } from 'react-router-dom';

import * as S from '@/pages/common/PageLayout/PageLayout.style';

import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import BottomNavigation from '@/components/common/BottomNavigation/BottomNavigation';
import HeaderLogo from '@/components/common/HeaderLogo/HeaderLogo';
import HeaderNav from '@/components/common/HeaderNav/HeaderNav';
import Loading from '@/components/common/Loading/Loading';

import useAuth from '@/hooks/useAuth';
import useDevice from '@/hooks/useDevice';

import { SROnly } from '@/style/GlobalStyles';

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

  const divRef = useRef<HTMLDivElement>(null);
  const isInitialRender = useRef<boolean>(true);

  const [pathName, setPathName] = useState<string>('');

  useLayoutEffect(() => {
    if (isInitialRender.current) {
      isInitialRender.current = false;
      return;
    }

    divRef.current?.focus();

    setPathName(location.pathname.split('/')[1]);
  }, [location.pathname]);

  const pathNames = {
    '': '홈 페이지로 이동합니다.',
    products: '제품 검색 페이지로 이동합니다.',
    product: '제품 상세 페이지로 이동합니다.',
    profiles: '프로필 검색 페이지로 이동합니다.',
    profile: '프로필 상세 페이지로 이동합니다.',
  };

  return (
    <div tabIndex={-1} ref={divRef}>
      <SROnly aria-live="assertive">{pathNames[pathName]}</SROnly>
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
    </div>
  );
}

export default PageLayout;
