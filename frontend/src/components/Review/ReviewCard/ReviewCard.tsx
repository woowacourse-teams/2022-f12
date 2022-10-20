import { useReducer } from 'react';

import Rating from '@/components/common/Rating/Rating';
import UserNameTag from '@/components/common/UserNameTag/UserNameTag';

import ReviewBottomSheet from '@/components/Review/ReviewBottomSheet/ReviewBottomSheet';
import * as S from '@/components/Review/ReviewCard/ReviewCard.style';

import useAnimation from '@/hooks/useAnimation';
import useAuth from '@/hooks/useAuth';

import { GITHUB_IMAGE_SIZE_SEARCH_PARAM } from '@/constants/link';
import ROUTES from '@/constants/routes';

type Props = {
  reviewId: Review['id'];
  reviewData: Omit<Review, 'id'>;
  handleDelete?: (id: number) => void;
  handleEdit?: (reviewInput: ReviewInput, id: number) => Promise<void>;
  handleFocus?: () => void;
  index?: number;
  userNameVisible?: boolean;
};

function ReviewCard({
  reviewId,
  handleDelete,
  handleEdit,
  handleFocus,
  reviewData,
  index = 0,
  userNameVisible = true,
}: Props) {
  const { product, rating, content, author, createdAt, authorMatch = false } = reviewData;
  const { isLoggedIn } = useAuth();

  const [isEditSheetOpen, toggleEditSheetOpen] = useReducer((isSheetOpen: boolean) => {
    if (!isLoggedIn) return false;

    return !isSheetOpen;
  }, false);

  const [shouldSheetRender, handleSheetUnmount, sheetAnimationTrigger] =
    useAnimation(isEditSheetOpen);

  const createAtDate = new Date(createdAt);
  const formattedDate = `${createAtDate.getFullYear()}년 ${
    createAtDate.getMonth() + 1
  }월 ${createAtDate.getDate()}일`;

  const handleEditClick = () => {
    toggleEditSheetOpen();
  };

  const handleDeleteClick = () => {
    handleDelete(reviewId);
    handleFocus();
  };

  return (
    <S.Container index={index}>
      {product && (
        <S.ProductArea to={`${ROUTES.PRODUCT}/${product.id}`}>
          <S.ImageWrapper>
            <S.Image src={product.imageUrl} />
          </S.ImageWrapper>
          <S.Title>{product.name}</S.Title>
        </S.ProductArea>
      )}
      <S.ReviewArea isFull={!product}>
        <S.Wrapper>
          <S.UserWrapper>
            {userNameVisible && (
              <S.ProfileLink to={`${ROUTES.PROFILE}/${author.id}`}>
                <UserNameTag
                  imageUrl={`${author.imageUrl}${GITHUB_IMAGE_SIZE_SEARCH_PARAM.small}`}
                  username={author.gitHubId}
                />
              </S.ProfileLink>
            )}
            {!product && authorMatch && (
              <S.ReviewModifyButtonWrapper>
                <S.ReviewModifyButton onClick={handleEditClick}>
                  수정
                </S.ReviewModifyButton>
                <S.ReviewModifyButton onClick={handleDeleteClick}>
                  삭제
                </S.ReviewModifyButton>
              </S.ReviewModifyButtonWrapper>
            )}
          </S.UserWrapper>
        </S.Wrapper>
        <div style={{ alignSelf: 'flex-end' }}>
          <Rating type="정수" rating={rating} />
        </div>
        <S.CreatedAt>{formattedDate}</S.CreatedAt>
        <S.Content>{content}</S.Content>
      </S.ReviewArea>
      {shouldSheetRender && (
        <ReviewBottomSheet
          handleClose={toggleEditSheetOpen}
          handleEdit={handleEdit}
          isEdit
          id={reviewId}
          rating={rating}
          content={content}
          handleUnmount={handleSheetUnmount}
          animationTrigger={sheetAnimationTrigger}
        />
      )}
    </S.Container>
  );
}

export default ReviewCard;
