import ProductDetail from '../../components/common/ProductDetail/ProductDetail';
import * as S from './Product.style';

import ReviewForm from '../../components/common/ReviewForm/ReviewForm';
import ReviewListSection from '../../components/ReviewListSection/ReviewListSection';
import useGetOne from '../../hooks/useGetOne';
import useGetMany from '../../hooks/useGetMany';

type Product = {
  id: number;
  name: string;
  imageUrl: string;
  rating: number;
};

type Review = {
  id: number;
  profileImage: string;
  username: string;
  rating: number;
  content: string;
};

function Product() {
  const product = useGetOne<Product>({ url: '/api/v1/keyboards/1' });

  const [reviews, getNextPage] = useGetMany<Review>({
    url: '/api/v1/keyboards/1/reviews',
    size: 6,
  });

  return (
    !!product && (
      <>
        <S.Container>
          <ProductDetail
            productImage={product.imageUrl}
            name={product.name}
            rating={product.rating}
          />
          <S.Wrapper>
            <ReviewForm />
            <ReviewListSection data={reviews} />
          </S.Wrapper>
        </S.Container>
      </>
    )
  );
}

export default Product;
