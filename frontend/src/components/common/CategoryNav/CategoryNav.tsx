import * as S from '@/components/common/CategoryNav/CategoryNav.style';
import ROUTES from '@/constants/routes';
import { Link } from 'react-router-dom';

type Props = {
  handleTransitionEnd: () => void;
  triggerAnimation: boolean;
};

const CATEGORY = {
  KEYBOARD: { key: 'keyboard', name: '키보드' },
  MOUSE: { key: 'mouse', name: '마우스' },
  MONITOR: { key: 'monitor', name: '모니터' },
  STAND: { key: 'stand', name: '거치대' },
  SOFTWARE: { key: 'software', name: '소프트웨어' },
} as const;

function CategoryNav({ handleTransitionEnd, triggerAnimation }: Props) {
  const CategoryLinks = Object.values(CATEGORY).map(({ key, name }) => (
    <Link key={key} to={`${ROUTES.PRODUCTS}?category=${key}`}>
      {name}
    </Link>
  ));
  return (
    <S.Container
      onTransitionEnd={handleTransitionEnd}
      triggerAnimation={triggerAnimation}
    >
      <S.Wrapper>
        <Link to={ROUTES.PRODUCTS}>전체 상품</Link>
        {CategoryLinks}
      </S.Wrapper>
    </S.Container>
  );
}

export default CategoryNav;
