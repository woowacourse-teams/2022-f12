import { Link } from 'react-router-dom';

import * as S from '@/components/common/CategoryNav/CategoryNav.style';

import { CATEGORIES } from '@/constants/product';
import ROUTES from '@/constants/routes';

type Props = {
  handleTransitionEnd: () => void;
  triggerAnimation: boolean;
};

function CategoryNav({ handleTransitionEnd, triggerAnimation }: Props) {
  const CategoryLinks = Object.entries(CATEGORIES).map(([key, name]) => (
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
