import { Outlet } from 'react-router-dom';
import HeaderLogo from '@/components/common/HeaderLogo/HeaderLogo';
import HeaderNav from '@/components/common/HeaderNav/HeaderNav';
import * as S from '@/pages/common/PageLayout/PageLayout.style';

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
