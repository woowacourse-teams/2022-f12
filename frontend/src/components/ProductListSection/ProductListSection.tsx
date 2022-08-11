import React from 'react';

import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import InfiniteScroll from '@/components/common/InfiniteScroll/InfiniteScroll';
import Loading from '@/components/common/Loading/Loading';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';

import ProductList from '@/components/ProductList/ProductList';
import * as S from '@/components/ProductListSection/ProductListSection.style';

type Props = {
  title: string;
  addOn?: React.ReactNode;
  data: Product[];
  isLoading: boolean;
  isReady: boolean;
  isError: boolean;
  getNextPage?: () => void;
};

function ProductListSection({
  title,
  addOn,
  data,
  isLoading,
  isReady,
  isError,
  getNextPage,
}: Props) {
  const isSinglePage = getNextPage === undefined;

  return (
    <S.Container aria-label={title}>
      <SectionHeader>
        <S.Title>{title}</S.Title>
        {addOn}
      </SectionHeader>
      <S.Wrapper>
        <AsyncWrapper fallback={<Loading />} isReady={isReady} isError={isError}>
          {isSinglePage ? (
            <ProductList data={data} />
          ) : (
            <InfiniteScroll
              handleContentLoad={getNextPage}
              isLoading={isLoading}
              isError={isError}
            >
              <ProductList data={data} />
            </InfiniteScroll>
          )}
        </AsyncWrapper>
      </S.Wrapper>
    </S.Container>
  );
}

export default ProductListSection;
