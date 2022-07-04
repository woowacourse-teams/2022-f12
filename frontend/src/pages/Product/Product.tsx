import ProductDetail from '../../components/common/ProductDetail/ProductDetail';
import * as S from './Product.style';

import { product, reviews } from './mockData';
import ReviewForm from '../../components/common/ReviewForm/ReviewForm';
import ReviewListSection from '../../components/ReviewListSection/ReviewListSection';
import Header from '../../components/common/Header/Header';

function Product() {
  return (
    <>
      <Header />
      <S.Container>
        <ProductDetail
          productImage={product.productImage}
          name={product.name}
          rating={product.rating}
        />
        <S.Wrapper>
          <ReviewForm />
          <ReviewListSection data={reviews} />
        </S.Wrapper>
      </S.Container>
    </>
  );
}

export default Product;
