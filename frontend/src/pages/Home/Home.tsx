import CustomLink from '@/components/common/CustomLink/CustomLink';
import ProductListSection from '@/components/ProductListSection/ProductListSection';
import ReviewListSection from '@/components/ReviewListSection/ReviewListSection';
import ROUTES from '@/constants/routes';
import useProducts from '@/hooks/useProducts';
import useReviews from '@/hooks/useReviews';

function Home() {
  const [keyboards] = useProducts({ size: 5, sort: 'rating,desc' });
  const [reviews, getNextPage] = useReviews({ size: 6 });

  const moreProductsLink = (
    <CustomLink to={ROUTES.PRODUCTS}>+더보기</CustomLink>
  );

  return (
    <>
      <ProductListSection
        title={'인기 있는 상품'}
        data={!!keyboards && keyboards}
        addOn={moreProductsLink}
      />
      <ReviewListSection columns={2} data={reviews} getNextPage={getNextPage} />
    </>
  );
}

export default Home;
