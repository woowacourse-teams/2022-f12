import { useReducer, useRef } from 'react';
import { useParams } from 'react-router-dom';

import * as S from '@/pages/Product/Product.style';

import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import BarGraph from '@/components/common/BarGraph/BarGraph';
import FloatingButton from '@/components/common/FloatingButton/FloatingButton';
import Loading from '@/components/common/Loading/Loading';
import ProductDetail from '@/components/common/ProductDetail/ProductDetail';
import StickyWrapper from '@/components/common/StickyWrapper/StickyWrapper';

import ReviewBottomSheet from '@/components/ReviewBottomSheet/ReviewBottomSheet';
import ReviewListSection from '@/components/ReviewListSection/ReviewListSection';

import useAuth from '@/hooks/useAuth';
import useModal from '@/hooks/useModal';
import useProduct from '@/hooks/useProduct';
import useReviews from '@/hooks/useReviews';
import useStatistics from '@/hooks/useStatistics';

import theme from '@/style/theme';

import Plus from '@/assets/plus.svg';

function Product() {
  const { isLoggedIn } = useAuth();
  const { productId: id } = useParams();
  const productId = Number(id);

  const [product, isProductReady, isProductError] = useProduct({
    productId: Number(productId),
  });
  const {
    reviews,
    isLoading: isReviewLoading,
    isReady: isReviewReady,
    isError: isReviewError,
    getNextPage,
    refetch: refetchReview,
    postReview,
    deleteReview,
    putReview: editReview,
  } = useReviews({
    size: '6',
    productId,
  });
  const [statistics, isStatisticsReady, isStatisticsError] = useStatistics({
    productId: Number(productId),
  });

  const [isSheetOpen, toggleSheetOpen] = useReducer(
    (isSheetOpen: boolean) => !isSheetOpen,
    false
  );
  const { showAlert, getConfirm } = useModal();

  const reviewListRef = useRef<HTMLDivElement | null>();

  const handleReviewSubmit = async (reviewInput: ReviewInput) => {
    await postReview(reviewInput);
    showAlert('리뷰가 작성되었습니다.');
    refetchReview();

    const topOfElement = reviewListRef.current;
    window.scrollTo({
      top: topOfElement && Number(topOfElement.offsetTop),
      behavior: 'smooth',
    });
  };

  const handleReviewEdit = async (reviewInput: ReviewInput, id: number) => {
    await editReview(reviewInput, id);
    showAlert('리뷰가 수정되었습니다.');
    refetchReview();
  };

  const handleReviewDeletion = async (id: number) => {
    const confirmation = await getConfirm('리뷰를 삭제하시겠습니까?');
    if (!confirmation) return;

    deleteReview(id)
      .then(() => {
        refetchReview();
      })
      .catch((error) => {
        console.log(error);
      });
  };

  return (
    <S.Container>
      <StickyWrapper>
        <S.ProductDetailWrapper>
          <AsyncWrapper
            fallback={<Loading />}
            isReady={isProductReady}
            isError={isProductError}
          >
            <ProductDetail product={product} />
          </AsyncWrapper>
          <S.BarGraphWrapper>
            <AsyncWrapper
              fallback={<Loading />}
              isReady={isStatisticsReady}
              isError={isStatisticsError}
            >
              <BarGraph statistics={statistics} />
            </AsyncWrapper>
          </S.BarGraphWrapper>
        </S.ProductDetailWrapper>
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
          isLoading={isReviewLoading}
          isReady={isReviewReady}
          isError={isReviewError}
        />
        {isSheetOpen && isLoggedIn && (
          <ReviewBottomSheet
            handleClose={toggleSheetOpen}
            handleSubmit={handleReviewSubmit}
            isEdit={false}
          />
        )}
      </S.Wrapper>
    </S.Container>
  );
}

export default Product;
