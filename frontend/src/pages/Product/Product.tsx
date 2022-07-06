import ProductDetail from '../../components/common/ProductDetail/ProductDetail';
import * as S from './Product.style';

import ReviewForm from '../../components/common/ReviewForm/ReviewForm';
import ReviewListSection from '../../components/ReviewListSection/ReviewListSection';
import useGetOne from '../../hooks/api/useGetOne';
import useGetMany from '../../hooks/api/useGetMany';
import usePost from '../../hooks/api/usePost';

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

type ReviewInput = {
  content: string;
  rating: number;
};

function Product() {
  const product = useGetOne<Product>({ url: '/api/v1/keyboards/1' });

  const [reviews, getNextPage] = useGetMany<Review>({
    url: '/api/v1/keyboards/1/reviews',
    size: 6,
  });

  const postReview = usePost<ReviewInput>({
    url: '/api/v1/keyboards/1/reviews',
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
