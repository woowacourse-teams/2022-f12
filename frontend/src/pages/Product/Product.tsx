import ProductDetail from '../../components/common/ProductDetail/ProductDetail';
import * as S from './Product.style';

import ReviewForm from '../../components/common/ReviewForm/ReviewForm';
import ReviewListSection from '../../components/ReviewListSection/ReviewListSection';
import useReviews from '../../hooks/useReviews';
import useProduct from '../../hooks/useProduct';

function Product() {
  const product = useProduct({ productId: 1 });

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
