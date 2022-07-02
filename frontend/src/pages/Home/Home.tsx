import Header from '../../components/common/Header/Header';
import ProductListSection from '../../components/ProductListSection/ProductListSection';
import ReviewListSection from '../../components/ReviewListSection/ReviewListSection';
import { products, reviews } from './mockData';
import * as S from './Home.style';

function Home() {
  return (
    <div>
      <Header />
      <S.Main>
        <ProductListSection data={products} />
        <ReviewListSection data={reviews} />
      </S.Main>
    </div>
  );
}

export default Home;
