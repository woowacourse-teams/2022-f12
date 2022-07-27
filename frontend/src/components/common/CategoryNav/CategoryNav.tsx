import * as S from '@/components/common/CategoryNav/CategoryNav.style';
import ROUTES from '@/constants/routes';
import { Link } from 'react-router-dom';

type Props = {
  handleTransitionEnd: () => void;
  triggerAnimation: boolean;
};

function CategoryNav({ handleTransitionEnd, triggerAnimation }: Props) {
  return (
    <S.Container
      onTransitionEnd={handleTransitionEnd}
      triggerAnimation={triggerAnimation}
    >
      <S.Wrapper>
        <Link to={ROUTES.PRODUCTS}>전체 상품</Link>
        <Link to={ROUTES.PRODUCTS}>키보드</Link>
        <Link to={ROUTES.PRODUCTS}>마우스</Link>
        <Link to={ROUTES.PRODUCTS}>모니터</Link>
        <Link to={ROUTES.PRODUCTS}>거치대</Link>
        <Link to={ROUTES.PRODUCTS}>소프트웨어</Link>
      </S.Wrapper>
    </S.Container>
  );
}

export default CategoryNav;
