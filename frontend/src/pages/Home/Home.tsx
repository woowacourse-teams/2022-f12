import * as S from '@/pages/Home/Home.style';

import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import Loading from '@/components/common/Loading/Loading';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';

import ProductListSection from '@/components/Product/ProductListSection/ProductListSection';
import ReviewListSection from '@/components/Review/ReviewListSection/ReviewListSection';

import usePopularProducts from '@/hooks/usePopularProducts';
import useReviews from '@/hooks/useReviews';

import TITLE from '@/constants/header';

const HOME_PRODUCT_SIZE = 4;
const HOME_REVIEW_SIZE = 6;

function Home() {
  const {
    products,
    isError: isPopularProductError,
    isLoading: isPopularProductLoading,
    isReady: isPopularProductReady,
  } = usePopularProducts({
    size: String(HOME_PRODUCT_SIZE),
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
          isReady={isPopularProductReady}
          isError={isPopularProductError}
        >
          <ProductListSection
            title={TITLE.POPULAR_PRODUCT}
            data={products}
            isLoading={isPopularProductLoading}
            isError={isPopularProductError}
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
            productVisible={true}
          />
        </AsyncWrapper>
      </S.SectionWrapper>
    </>
  );
}

export default Home;
