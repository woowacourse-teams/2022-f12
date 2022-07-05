import CustomLink from '../../components/common/CustomLink/CustomLink';
import ProductListSection from '../../components/ProductListSection/ProductListSection';
import ReviewListSection from '../../components/ReviewListSection/ReviewListSection';
import ROUTES from '../../constants/routes';
import useGetMany from '../../hooks/useGetMany';

type Product = {
  id: number;
  name: string;
  productImage: string;
  rating: number;
};

type Reviews = {
  id: number;
  profileImage: string;
  username: string;
  rating: number;
  content: string;
};

function Home() {
  const [keyboards] = useGetMany<Product>({
    url: '/api/v1/keyboards',
    size: 5,
  });

  const [reviews, getNextPage] = useGetMany<Reviews>({
    url: '/api/v1/reviews/',
    size: 6,
  });

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
      <ReviewListSection data={reviews} />
    </>
  );
}

export default Home;
