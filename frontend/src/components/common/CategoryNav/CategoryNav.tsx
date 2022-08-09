import * as S from '@/components/common/CategoryNav/CategoryNav.style';
import ROUTES from '@/constants/routes';
import { Link } from 'react-router-dom';

type Props = {
  handleTransitionEnd: () => void;
  triggerAnimation: boolean;
};

export const CATEGORY = {
  keyboard: '키보드',
  mouse: '마우스',
  monitor: '모니터',
  stand: '거치대',
  software: '소프트웨어',
} as const;

function CategoryNav({ handleTransitionEnd, triggerAnimation }: Props) {
  const CategoryLinks = Object.entries(CATEGORY).map(([key, name]) => (
    <Link key={key} to={`${ROUTES.PRODUCTS}?category=${key}`} aria-label={name}>
      {name}
    </Link>
  ));
  return (
    <S.Container
      onTransitionEnd={handleTransitionEnd}
      triggerAnimation={triggerAnimation}
    >
      <S.Wrapper>{CategoryLinks}</S.Wrapper>
    </S.Container>
  );
}

export default CategoryNav;
