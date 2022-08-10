import ProductListSection from '@/components/ProductListSection/ProductListSection';
import ReviewListSection from '@/components/ReviewListSection/ReviewListSection';
import useProducts from '@/hooks/useProducts';
import useReviews from '@/hooks/useReviews';

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
      <ProductListSection
        title={'인기 있는 상품'}
        data={products}
        isLoading={isProductLoading}
        isReady={isProductReady}
        isError={isProductError}
      />
      <ReviewListSection
        columns={2}
        data={reviews}
        getNextPage={getNextPage}
        isLoading={isReviewLoading}
        isReady={isReviewReady}
        isError={isReviewError}
      />
    </>
  );
}

export default Home;
