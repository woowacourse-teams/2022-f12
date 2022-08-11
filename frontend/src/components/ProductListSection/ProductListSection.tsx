import Masonry from '../common/Masonry/Masonry';
import NoDataPlaceholder from '../common/NoDataPlaceholder/NoDataPlaceholder';
import ProductCard from '../common/ProductCard/ProductCard';
import { Link } from 'react-router-dom';

import InfiniteScroll from '@/components/common/InfiniteScroll/InfiniteScroll';

import * as S from '@/components/ProductListSection/ProductListSection.style';

import ROUTES from '@/constants/routes';

type Props = {
  data: Product[];
  isLoading: boolean;
  isError: boolean;
  getNextPage?: () => void;
};

function ProductListSection({ data, isLoading, isError, getNextPage }: Props) {
  const isSinglePage = getNextPage === undefined;
  const productList = (
    <Masonry columnCount={4}>
      {data.map(({ id, imageUrl, name, rating, reviewCount }) => (
        <Link to={`${ROUTES.PRODUCT}/${id}`} key={id}>
          <ProductCard
            imageUrl={imageUrl}
            name={name}
            rating={rating}
            reviewCount={reviewCount}
          />
        </Link>
      ))}
      {data.length === 0 && <NoDataPlaceholder />}
    </Masonry>
  );

  return (
    <S.Container>
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
