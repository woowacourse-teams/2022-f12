import Header from '../../components/common/Header/Header';
import CustomLink from '../../components/common/CustomLink/CustomLink';
import ProductListSection from '../../components/ProductListSection/ProductListSection';
import ReviewListSection from '../../components/ReviewListSection/ReviewListSection';
import { products, reviews } from './mockData';
import * as S from './Home.style';

function Home() {
  const moreProductsLink = <CustomLink to="products">+더보기</CustomLink>;
  return (
    <>
      <Header />
      <S.Main>
        <ProductListSection
          title={'인기 있는 상품'}
          data={products}
          addOn={moreProductsLink}
        />
        <ReviewListSection data={reviews} />
      </S.Main>
    </>
  );
}

export default Home;
