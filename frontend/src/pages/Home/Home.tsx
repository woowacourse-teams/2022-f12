import * as S from '@/pages/Home/Home.style';

import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import Loading from '@/components/common/Loading/Loading';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';

import ProductListSection from '@/components/Product/ProductListSection/ProductListSection';
import ReviewListSection from '@/components/Review/ReviewListSection/ReviewListSection';

import useProducts from '@/hooks/useProducts';
import useReviews from '@/hooks/useReviews';

import TITLE from '@/constants/header';

const HOME_PRODUCT_SIZE = 4;
const HOME_REVIEW_SIZE = 6;

function Home() {
  const {
    products,
    isError: isProductError,
    isLoading: isProductLoading,
    isReady: isProductReady,
  } = useProducts({
    size: String(HOME_PRODUCT_SIZE),
    sort: 'rating,desc',
  });
  const {
    reviews,
    getNextPage,
    isLoading: isReviewLoading,
    isReady: isReviewReady,
    isError: isReviewError,
  } = useReviews({ size: String(HOME_REVIEW_SIZE) });

  return (
    <>
      <S.SectionWrapper>
        <SectionHeader title={TITLE.POPULAR_PRODUCT} />
        <AsyncWrapper
          fallback={<Loading />}
          isReady={isProductReady}
          isError={isProductError}
        >
          <ProductListSection
            title={TITLE.POPULAR_PRODUCT}
            data={products}
            isLoading={isProductLoading}
            isError={isProductError}
            pageSize={HOME_PRODUCT_SIZE}
            displayType={'flex'}
          />
        </AsyncWrapper>
      </S.SectionWrapper>
      <S.SectionWrapper>
        <SectionHeader title={TITLE.RECENT_REVIEW} />
        <AsyncWrapper
          fallback={<Loading />}
          isReady={isReviewReady}
          isError={isReviewError}
        >
          <ReviewListSection
            columns={2}
            data={reviews}
            getNextPage={getNextPage}
            isLoading={isReviewLoading}
            isError={isReviewError}
            pageSize={HOME_REVIEW_SIZE}
          />
        </AsyncWrapper>
      </S.SectionWrapper>
    </>
  );
}

export default Home;
