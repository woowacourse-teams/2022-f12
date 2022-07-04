import CustomLink from '../../components/common/CustomLink/CustomLink';
import ProductListSection from '../../components/ProductListSection/ProductListSection';
import ReviewListSection from '../../components/ReviewListSection/ReviewListSection';
import ROUTES from '../../constants/routes';
import { products, reviews } from './mockData';

function Home() {
  const moreProductsLink = (
    <CustomLink to={ROUTES.PRODUCTS}>+더보기</CustomLink>
  );
  return (
    <>
      <ProductListSection
        title={'인기 있는 상품'}
        data={products}
        addOn={moreProductsLink}
      />
      <ReviewListSection data={reviews} />
    </>
  );
}

export default Home;
