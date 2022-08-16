import { useReducer } from 'react';

import Rating from '@/components/common/Rating/Rating';
import UserNameTag from '@/components/common/UserNameTag/UserNameTag';

import ReviewBottomSheet from '@/components/Review/ReviewBottomSheet/ReviewBottomSheet';
import * as S from '@/components/Review/ReviewCard/ReviewCard.style';

import useAuth from '@/hooks/useAuth';

import ROUTES from '@/constants/routes';

type Props = {
  loginUserGithubId: Member['gitHubId'];
  reviewId: Review['id'];
  reviewData: Omit<Review, 'id'>;
  handleDelete?: (id: number) => void;
  handleEdit?: (reviewInput: ReviewInput, id: number) => Promise<void>;
  animationTrigger?: boolean;
  index?: number;
};

function ReviewCard({
  reviewId,
  loginUserGithubId,
  handleDelete,
  handleEdit,
  reviewData,
  animationTrigger = true,
  index = 0,
}: Props) {
  const { product, rating, content, author, createdAt } = reviewData;
  const { isLoggedIn } = useAuth();

  const [isEditSheetOpen, toggleEditSheetOpen] = useReducer((isSheetOpen: boolean) => {
    if (!isLoggedIn) return false;

    return !isSheetOpen;
  }, false);

  const createAtDate = new Date(createdAt);
  const formattedDate = `${createAtDate.getFullYear()}년 ${
    createAtDate.getMonth() + 1
  }월 ${createAtDate.getDate()}일`;

  const handleEditClick = () => {
    toggleEditSheetOpen();
  };

  const handleDeleteClick = () => {
    handleDelete(reviewId);
  };

  return (
    <S.Container animationTrigger={animationTrigger} index={index}>
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
            <S.ProfileLink to={`${ROUTES.PROFILE}/${author.id}`}>
              <UserNameTag imageUrl={author.imageUrl} username={author.gitHubId} />
            </S.ProfileLink>
            {!product && loginUserGithubId === author.gitHubId && (
              <>
                <S.EditButton onClick={handleEditClick}>수정</S.EditButton>
                <S.DeleteButton onClick={handleDeleteClick}>삭제</S.DeleteButton>
              </>
            )}
          </S.UserWrapper>
          <Rating type="정수" rating={rating} />
        </S.Wrapper>
        <S.CreatedAt>{formattedDate}</S.CreatedAt>
        <S.Content>{content}</S.Content>
      </S.ReviewArea>
      {isEditSheetOpen && (
        <ReviewBottomSheet
          handleClose={toggleEditSheetOpen}
          handleEdit={handleEdit}
          isEdit
          id={reviewId}
          rating={rating}
          content={content}
        />
      )}
    </S.Container>
  );
}

export default ReviewCard;
