import React from 'react';
import { Link } from 'react-router-dom';
import ProductCard from '../common/ProductCard/ProductCard';
import SectionHeader from '../common/SectionHeader/SectionHeader';

import * as S from './ProductListSection.style';
import ROUTES from '../../constants/routes';
import InfiniteScroll from '../common/InfiniteScroll/InfiniteScroll';
import Masonry from '../common/Masonry/Masonry';

type Props = {
  title: string;
  addOn?: React.ReactNode;
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
    data.length !== 0 && (
      <S.Container>
        <SectionHeader>
          <S.Title>{title}</S.Title>
          {addOn}
        </SectionHeader>
        <S.Wrapper>
          {getNextPage !== undefined ? (
            <InfiniteScroll handleContentLoad={getNextPage}>
              <Masonry columnCount={4}>{productCardList}</Masonry>
            </InfiniteScroll>
          ) : (
            <Masonry columnCount={4}>{productCardList}</Masonry>
          )}
        </S.Wrapper>
      </S.Container>
    )
  );
}

export default ProductListSection;
