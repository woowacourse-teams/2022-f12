import React from 'react';
import { Link } from 'react-router-dom';
import ProductCard from '@/components/common/ProductCard/ProductCard';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';

import * as S from '@/components/ProductListSection/ProductListSection.style';
import ROUTES from '@/constants/routes';
import InfiniteScroll from '@/components/common/InfiniteScroll/InfiniteScroll';
import Masonry from '@/components/common/Masonry/Masonry';
import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import Loading from '@/components/common/Loading/Loading';

type Props = {
  title: string;
  addOn?: React.ReactNode;
  data: Product[];
  isLoading: boolean;
  isReady: boolean;
  getNextPage?: () => void;
};

function ProductListSection({
  title,
  addOn,
  data,
  isLoading,
  isReady,
  getNextPage,
}: Props) {
  const ProductCardList = (data: Product[]) => {
    return data.map(({ id, imageUrl, name, rating }) => (
      <Link to={`${ROUTES.PRODUCT}/${id}`} key={id}>
        <ProductCard productImage={imageUrl} name={name} rating={rating} />
      </Link>
    ));
  };

  return (
    <S.Container>
      <SectionHeader>
        <S.Title>{title}</S.Title>
        {addOn}
      </SectionHeader>
      <S.Wrapper>
        <AsyncWrapper fallback={<Loading />} isReady={isReady}>
          {getNextPage !== undefined ? (
            <InfiniteScroll
              handleContentLoad={getNextPage}
              isLoading={isLoading}
            >
              <Masonry columnCount={4}>{ProductCardList(data)}</Masonry>
            </InfiniteScroll>
          ) : (
            <Masonry columnCount={4}>{ProductCardList(data)}</Masonry>
          )}
        </AsyncWrapper>
      </S.Wrapper>
    </S.Container>
  );
}

export default ProductListSection;
