import Rating from '@/components/common/Rating/Rating';
import UserNameTag from '@/components/common/UserNameTag/UserNameTag';
import { useReducer } from 'react';
import ReviewBottomSheet from '@/components/ReviewBottomSheet/ReviewBottomSheet';

import * as S from '@/components/common/ReviewCard/ReviewCard.style';
import useAuth from '@/hooks/useAuth';
import ROUTES from '@/constants/routes';

type Props = {
  product?: {
    id: number;
    name: string;
    imageUrl: string;
  };
  author: Review['author'];
  rating: number;
  content: string;
  loginUserGithubId: string;
  reviewId: number;
  handleDelete?: (id: number) => void;
  handleEdit?: (reviewInput: ReviewInput, id: number) => void;
};

function ReviewCard({
  reviewId,
  product,
  rating,
  content,
  author,
  loginUserGithubId,
  handleDelete,
  handleEdit,
}: Props) {
  const [isEditSheetOpen, toggleEditSheetOpen] = useReducer(
    (isSheetOpen: boolean) => !isSheetOpen,
    false
  );

  const { isLoggedIn } = useAuth();

  return (
    <S.Container>
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
              <UserNameTag
                imageUrl={author.imageUrl}
                username={author.gitHubId}
              />
            </S.ProfileLink>
            {!product && loginUserGithubId === author.gitHubId && (
              <>
                <S.EditButton
                  onClick={() => {
                    toggleEditSheetOpen();
                  }}
                >
                  수정
                </S.EditButton>
                <S.DeleteButton
                  onClick={() => {
                    handleDelete(reviewId);
                  }}
                >
                  삭제
                </S.DeleteButton>
              </>
            )}
          </S.UserWrapper>
          <Rating type="정수" rating={rating} />
        </S.Wrapper>
        <S.Content>{content}</S.Content>
      </S.ReviewArea>
      {isEditSheetOpen && isLoggedIn && (
        <ReviewBottomSheet
          handleClose={toggleEditSheetOpen}
          handleEdit={handleEdit}
          isEdit
          reviewId={reviewId}
          rating={rating}
          content={content}
        />
      )}
    </S.Container>
  );
}

export default ReviewCard;
