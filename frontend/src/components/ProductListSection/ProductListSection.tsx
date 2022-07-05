import React from 'react';
import { Link } from 'react-router-dom';
import ProductCard from '../common/ProductCard/ProductCard';
import SectionHeader from '../common/SectionHeader/SectionHeader';

import * as S from './ProductListSection.style';
import ROUTES from '../../constants/routes';

type Props = {
  title: string;
  addOn: React.ReactNode;
  data: { id: number; productImage: string; name: string; rating: number }[];
};

function ProductListSection({ title, addOn, data }: Props) {
  const productCardList =
    data &&
    data.map(({ id, productImage, name, rating }) => (
      <Link to={ROUTES.PRODUCT} key={id}>
        <ProductCard productImage={productImage} name={name} rating={rating} />
      </Link>
    ));

  return (
    <S.Container>
      <SectionHeader>
        <S.Title>{title}</S.Title>
        {addOn}
      </SectionHeader>
      <S.Wrapper>{productCardList}</S.Wrapper>
    </S.Container>
  );
}

export default ProductListSection;
