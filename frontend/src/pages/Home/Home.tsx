import CustomLink from '@/components/common/CustomLink/CustomLink';
import ProductListSection from '@/components/ProductListSection/ProductListSection';
import ReviewListSection from '@/components/ReviewListSection/ReviewListSection';
import ROUTES from '@/constants/routes';
import useProducts from '@/hooks/useProducts';
import useReviews from '@/hooks/useReviews';

function Home() {
  const {
    products,
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
  } = useReviews({ size: '6' });

  const moreProductsLink = (
    <CustomLink to={ROUTES.PRODUCTS}>+더보기</CustomLink>
  );

  return (
    <>
      <ProductListSection
        title={'인기 있는 상품'}
        data={products}
        isLoading={isProductLoading}
        isReady={isProductReady}
        addOn={moreProductsLink}
      />
      <ReviewListSection
        columns={2}
        data={reviews}
        getNextPage={getNextPage}
        isLoading={isReviewLoading}
        isReady={isReviewReady}
      />
    </>
  );
}

export default Home;
