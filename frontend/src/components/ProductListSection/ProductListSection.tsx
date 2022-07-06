import React from 'react';
import { Link } from 'react-router-dom';
import ProductCard from '../common/ProductCard/ProductCard';
import SectionHeader from '../common/SectionHeader/SectionHeader';

import * as S from './ProductListSection.style';
import ROUTES from '../../constants/routes';
import InfiniteScroll from '../common/InfiniteScroll/InfiniteScroll';

type Props = {
  title: string;
  addOn: React.ReactNode;
  data: { id: number; imageUrl: string; name: string; rating: number }[];
  getNextPage?: () => void;
};

function ProductListSection({ title, addOn, data, getNextPage }: Props) {
  const productCardList =
    data &&
    data.map(({ id, imageUrl, name, rating }) => (
      <Link to={`${ROUTES.PRODUCT}/${id}`} key={id}>
        <ProductCard productImage={imageUrl} name={name} rating={rating} />
      </Link>
    ));

  return (
    <S.Container>
      <SectionHeader>
        <S.Title>{title}</S.Title>
        {addOn}
      </SectionHeader>
      <S.Wrapper>
        <InfiniteScroll handleContentLoad={getNextPage}>
          {productCardList}
        </InfiniteScroll>
      </S.Wrapper>
    </S.Container>
  );
}

export default ProductListSection;
