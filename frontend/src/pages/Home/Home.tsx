import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import Loading from '@/components/common/Loading/Loading';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';

import ProductListSection from '@/components/Product/ProductListSection/ProductListSection';
import ReviewListSection from '@/components/Review/ReviewListSection/ReviewListSection';

import useProducts from '@/hooks/useProducts';
import useReviews from '@/hooks/useReviews';

import TITLE from '@/constants/header';

function Home() {
  const {
    products,
    isError: isProductError,
    isLoading: isProductLoading,
    isReady: isProductReady,
  } = useProducts({
    size: '4',
    sort: 'rating,desc',
  });
  const {
    reviews,
    getNextPage,
    isLoading: isReviewLoading,
    isReady: isReviewReady,
    isError: isReviewError,
  } = useReviews({ size: '6' });

  return (
    <>
      <SectionHeader title={TITLE.POPULAR_PRODUCT} />
      <AsyncWrapper
        fallback={<Loading />}
        isReady={isProductReady}
        isError={isProductError}
      >
        <ProductListSection
          data={products}
          isLoading={isProductLoading}
          isError={isProductError}
        />
      </AsyncWrapper>

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
        />
      </AsyncWrapper>
    </>
  );
}

export default Home;
