import ProductDetail from '../../components/common/ProductDetail/ProductDetail';
import * as S from './Product.style';

import ReviewForm from '../../components/common/ReviewForm/ReviewForm';
import ReviewListSection from '../../components/ReviewListSection/ReviewListSection';
import useGetOne from '../../hooks/api/useGetOne';
import useReviews from '../../hooks/useReviews';

type Product = {
  id: number;
  name: string;
  imageUrl: string;
  rating: number;
};

type ReviewInput = {
  content: string;
  rating: number;
};

function Product() {
  const product = useGetOne<Product>({ url: '/api/v1/keyboards/1' });

  const [reviews, getNextPage, postReview] = useReviews({
    size: 6,
    productId: 1,
  });

  const handleReviewSubmit = async (reviewInput: ReviewInput) => {
    await postReview(reviewInput);
  };

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
            <ReviewForm handleSubmit={handleReviewSubmit} />
            <ReviewListSection data={reviews} getNextPage={getNextPage} />
          </S.Wrapper>
        </S.Container>
      </>
    )
  );
}

export default Product;
