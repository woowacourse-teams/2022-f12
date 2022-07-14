import { Outlet } from 'react-router-dom';
import HeaderLogo from '@/components/common/HeaderLogo/HeaderLogo';
import HeaderNav from '@/components/common/HeaderNav/HeaderNav';
import * as S from './PageLayout.style';

function PageLayout() {
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
