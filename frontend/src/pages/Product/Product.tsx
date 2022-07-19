import ProductDetail from '@/components/common/ProductDetail/ProductDetail';
import * as S from '@/pages/Product/Product.style';

import ReviewListSection from '@/components/ReviewListSection/ReviewListSection';
import useReviews from '@/hooks/useReviews';
import useProduct from '@/hooks/useProduct';
import { useParams } from 'react-router-dom';
import StickyWrapper from '@/components/common/StickyWrapper/StickyWrapper';
import ReviewBottomSheet from '@/components/ReviewBottomSheet/ReviewBottomSheet';
import { useReducer, useRef } from 'react';
import FloatingButton from '@/components/common/FloatingButton/FloatingButton';
import Plus from '@/assets/plus.svg';
import theme from '@/style/theme';
import useAuth from '@/hooks/useAuth';

function Product() {
  const { isLoggedIn } = useAuth();
  const { productId: id } = useParams();
  const productId = Number(id);

  const product = useProduct({ productId: Number(productId) });
  const [
    reviews,
    getNextPage,
    refetchReview,
    postReview,
    deleteReview,
    editReview,
  ] = useReviews({
    size: 6,
    productId,
  });

  const [isSheetOpen, toggleSheetOpen] = useReducer(
    (isSheetOpen: boolean) => !isSheetOpen,
    false
  );

  const reviewListRef = useRef<HTMLDivElement | null>();

  const handleReviewSubmit = async (reviewInput: ReviewInput) => {
    await postReview(reviewInput);
    refetchReview();
    const topOfElement = reviewListRef.current;
    window.scrollTo({
      top: reviewListRef.current && Number(topOfElement.offsetTop),
      behavior: 'smooth',
    });
  };

  const handleReviewEdit = (reviewInput: ReviewInput, id: number) => {
    editReview(reviewInput, id)
      .then(() => {
        alert('리뷰가 수정되었습니다.');
        refetchReview();
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const handleReviewDeletion = (id: number) => {
    if (!confirm('리뷰를 삭제하시겠습니까?')) return;

    deleteReview(id)
      .then(() => {
        refetchReview();
      })
      .catch((error) => {
        console.log(error);
      });
  };

  return (
    !!product && (
      <>
        <S.Container>
          <StickyWrapper>
            <ProductDetail
              imageUrl={product.imageUrl}
              name={product.name}
              rating={product.rating}
            />
          </StickyWrapper>
          <S.Wrapper ref={reviewListRef}>
            {!isSheetOpen && isLoggedIn && (
              <FloatingButton clickHandler={toggleSheetOpen}>
                <Plus stroke={theme.colors.white} />
              </FloatingButton>
            )}
            <ReviewListSection
              columns={1}
              data={reviews}
              getNextPage={getNextPage}
              handleDelete={handleReviewDeletion}
              handleEdit={handleReviewEdit}
            />
            {isSheetOpen && (
              <ReviewBottomSheet
                handleClose={toggleSheetOpen}
                handleSubmit={handleReviewSubmit}
                isEdit={false}
              />
            )}
          </S.Wrapper>
        </S.Container>
      </>
    )
  );
}

export default Product;
