import { Link } from 'react-router-dom';

import InfiniteScroll from '@/components/common/InfiniteScroll/InfiniteScroll';
import Masonry from '@/components/common/Masonry/Masonry';
import NoDataPlaceholder from '@/components/common/NoDataPlaceholder/NoDataPlaceholder';

import ProductCard from '@/components/Product/ProductCard/ProductCard';
import * as S from '@/components/Product/ProductListSection/ProductListSection.style';

import ROUTES from '@/constants/routes';

type Props = Omit<DataFetchStatus, 'isReady'> & {
  title: string;
  data: Product[];
  getNextPage?: () => void;
  pageSize?: number;
};

function ProductListSection({
  title,
  data,
  isLoading,
  isError,
  getNextPage,
  pageSize = 12,
}: Props) {
  const isSinglePage = getNextPage === undefined;
  const productList = (
    <Masonry columnCount={4}>
      {data.map(({ id, imageUrl, name, rating, reviewCount }, index) => (
        <Link to={`${ROUTES.PRODUCT}/${id}`} key={id}>
          <ProductCard
            imageUrl={imageUrl}
            name={name}
            rating={rating}
            reviewCount={reviewCount}
            index={index % pageSize}
          />
        </Link>
      ))}
      {data.length === 0 && <NoDataPlaceholder />}
    </Masonry>
  );

  return (
    <S.Container aria-label={title}>
      <S.Wrapper>
        {isSinglePage ? (
          productList
        ) : (
          <InfiniteScroll
            handleContentLoad={getNextPage}
            isLoading={isLoading}
            isError={isError}
          >
            {productList}
          </InfiniteScroll>
        )}
      </S.Wrapper>
    </S.Container>
  );
}

export default ProductListSection;
