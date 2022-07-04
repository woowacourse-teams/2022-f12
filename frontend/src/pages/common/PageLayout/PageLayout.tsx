import { Outlet } from 'react-router-dom';
import Header from '../../../components/common/Header/Header';
import * as S from './PageLayout.style';

function PageLayout() {
  return (
    <>
      <Header />
      <S.Main>
        <Outlet />
      </S.Main>
    </>
  );
}

export default PageLayout;
