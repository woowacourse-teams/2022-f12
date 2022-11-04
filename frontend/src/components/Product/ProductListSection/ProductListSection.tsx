import { Link } from 'react-router-dom';

import InfiniteScroll from '@/components/common/InfiniteScroll/InfiniteScroll';
import NoDataPlaceholder from '@/components/common/NoDataPlaceholder/NoDataPlaceholder';

import ProductCard from '@/components/Product/ProductCard/ProductCard';
import * as S from '@/components/Product/ProductListSection/ProductListSection.style';

import useDevice from '@/hooks/useDevice';

import ROUTES from '@/constants/routes';

type Props = Omit<DataFetchStatus, 'isReady'> & {
  title: string;
  data: Product[];
  getNextPage?: () => void;
  displayType?: 'flex' | 'masonry';
  pageSize?: number;
};

const ROW_COUNT = (displayWidth: number) =>
  displayWidth >= 1440 ? 4 : displayWidth >= 768 ? 3 : displayWidth >= 428 ? 2 : 1;

const DEVICE_TO_SIZE = {
  desktop: 'l',
  tablet: 'm',
  mobile: 's',
} as const;

function ProductListSection({
  title,
  data,
  isLoading,
  isError,
  getNextPage,
  displayType = 'masonry',
  pageSize = 12,
}: Props) {
  const { device, displayWidth } = useDevice();
  const cardSize =
    displayType === 'flex' ? DEVICE_TO_SIZE[device] : device === 'tablet' ? 'm' : 'l';

  const productList = data.map(({ id, imageUrl, name, rating, reviewCount }, index) => (
    <S.ProductCardLi key={id}>
      <Link
        to={`${ROUTES.PRODUCT}/${id}`}
        key={id}
        target="_blank"
        rel="noopener noreferrer"
      >
        <ProductCard
          imageUrl={imageUrl}
          name={name}
          rating={rating}
          reviewCount={reviewCount}
          index={index % pageSize}
          size={cardSize}
        />
      </Link>
    </S.ProductCardLi>
  ));

  const isSinglePage = displayType === 'flex' || getNextPage === undefined;

  const Content = isSinglePage ? (
    <S.FlexWrapper>{productList}</S.FlexWrapper>
  ) : (
    <InfiniteScroll
      handleContentLoad={getNextPage}
      isLoading={isLoading}
      isError={isError}
    >
      <S.Grid
        columnCount={displayType === 'masonry' ? ROW_COUNT(displayWidth) : pageSize}
      >
        {productList}
      </S.Grid>
    </InfiniteScroll>
  );

  return (
    <S.Container aria-label={title}>
      <S.Wrapper>
        {data.length === 0 ? (
          <S.NoDataContainer>
            <NoDataPlaceholder />
          </S.NoDataContainer>
        ) : (
          Content
        )}
      </S.Wrapper>
    </S.Container>
  );
}

export default ProductListSection;
