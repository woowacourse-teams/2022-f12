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

const pathKoreanNames = {
  '': '홈',
  products: '제품 검색',
  product: '제품 상세',
  profiles: '프로필 검색',
  profile: '프로필 상세',
  me: '내 정보',
  login: '로그인',
  register: '추가 정보 입력',
} as const;

const isValidPath = (input: string): input is keyof typeof pathKoreanNames =>
  input in pathKoreanNames;

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

  const [pathName, setPathName] = useState<keyof typeof pathKoreanNames>(null);
  const [pathNameNotice, setPathNameNotice] = useState('');

  useLayoutEffect(() => {
    const tempPathName = location.pathname.split('/')[1];
    if (!isValidPath(tempPathName)) {
      setPathName(null);
      return;
    }

    setPathName(tempPathName);
  }, [location.pathname]);

  useLayoutEffect(() => {
    if (isInitialRender.current) {
      isInitialRender.current = false;
      return;
    }
    divRef.current?.focus();
    setPathNameNotice(`${pathKoreanNames[pathName]} 페이지로 이동합니다.`);
  }, [pathName]);

  useEffect(() => {
    document.title = `${pathKoreanNames[pathName]} | F12: 개발자의 모든 도구`;
  }, [pathName]);

  return (
    <S.SRFocusElement tabIndex={-1} ref={divRef}>
      <SROnly aria-live="assertive">{pathName && pathNameNotice}</SROnly>
      {device !== 'desktop' && <HeaderNav.Mobile />}

      {device === 'desktop' && (
        <>
          <HeaderLogo />
          <HeaderNav />
        </>
      )}
      <Suspense>
        <S.Main aria-label={`${pathKoreanNames[pathName]} 페이지`}>
          <AsyncWrapper isReady={isReady} fallback={<Loading />}>
            <Outlet />
          </AsyncWrapper>
        </S.Main>
      </Suspense>
      {device !== 'desktop' && <BottomNavigation />}
    </S.SRFocusElement>
  );
}

export default PageLayout;
