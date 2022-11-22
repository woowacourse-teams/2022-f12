import { createContext, PropsWithChildren, useContext, useReducer } from 'react';

import Rating from '@/components/common/Rating/Rating';
import UserNameTag from '@/components/common/UserNameTag/UserNameTag';

import ReviewBottomSheet from '@/components/Review/ReviewBottomSheet/ReviewBottomSheet';
import * as S from '@/components/Review/ReviewCard/ReviewCard.style';

import useAnimation from '@/hooks/useAnimation';
import useAuth from '@/hooks/useAuth';

import { GITHUB_IMAGE_SIZE_SEARCH_PARAM } from '@/constants/link';
import ROUTES from '@/constants/routes';

type Props = {
  reviewData: Review;
  index?: number;
  userNameVisible?: boolean;
  productVisible?: boolean;
};

const ReviewContentContext = createContext<Review>(null);

function ReviewCard({
  reviewData,
  index = 0,
  productVisible = false,
  userNameVisible = true,
  children,
}: PropsWithChildren<Props>) {
  const { product, rating, content, author, createdAt } = reviewData;

  const createAtDate = new Date(createdAt);
  const formattedDate = `${createAtDate.getFullYear()}년 ${
    createAtDate.getMonth() + 1
  }월 ${createAtDate.getDate()}일`;

  return (
    <S.Container index={index}>
      {productVisible && <ProductBlock product={product} />}
      <S.ReviewArea isFull={!product}>
        <S.Wrapper>
          <S.UserWrapper>
            {userNameVisible && <AuthorBlock author={author} />}
            <ReviewContentContext.Provider value={reviewData}>
              {children}
            </ReviewContentContext.Provider>
          </S.UserWrapper>
        </S.Wrapper>
        <div style={{ alignSelf: 'flex-end' }}>
          <Rating type="정수" rating={rating} />
        </div>
        <S.CreatedAt>{formattedDate}</S.CreatedAt>
        <S.Content>{content}</S.Content>
      </S.ReviewArea>
    </S.Container>
  );
}

function ProductBlock({ product }: { product: Review['product'] }) {
  return (
    <S.ProductArea to={`${ROUTES.PRODUCT}/${product.id}`}>
      <S.ImageWrapper>
        <S.Image src={product.imageUrl} />
      </S.ImageWrapper>
      <S.Title>{product.name}</S.Title>
    </S.ProductArea>
  );
}

function AuthorBlock({ author }: { author: Review['author'] }) {
  return (
    <S.ProfileLink to={`${ROUTES.PROFILE}/${author.id}`}>
      <UserNameTag
        imageUrl={`${author.imageUrl}${GITHUB_IMAGE_SIZE_SEARCH_PARAM.small}`}
        username={author.gitHubId}
      />
    </S.ProfileLink>
  );
}

type AuthorControlsProps = {
  handleDelete?: (id: number) => void;
  handleEdit?: (reviewInput: ReviewInput, id: number) => Promise<void>;
  handleFocus: () => void;
};

function AuthorControls({ handleDelete, handleEdit, handleFocus }: AuthorControlsProps) {
  const { isLoggedIn } = useAuth();
  const { id: reviewId, rating, content } = useContext(ReviewContentContext);
  const [isEditSheetOpen, toggleEditSheetOpen] = useReducer((isSheetOpen: boolean) => {
    if (!isLoggedIn) return false;

    return !isSheetOpen;
  }, false);

  const [shouldSheetRender, handleSheetUnmount, sheetAnimationTrigger] =
    useAnimation(isEditSheetOpen);

  const handleEditClick = () => {
    toggleEditSheetOpen();
  };

  const handleDeleteClick = () => {
    handleDelete(reviewId);
    handleFocus();
  };

  return (
    <>
      <S.ReviewModifyButtonWrapper>
        <S.ReviewModifyButton onClick={handleEditClick}>수정</S.ReviewModifyButton>
        <S.ReviewModifyButton onClick={handleDeleteClick}>삭제</S.ReviewModifyButton>
      </S.ReviewModifyButtonWrapper>
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
          handleFocus={handleFocus}
          handleSubmit={undefined}
        />
      )}
    </>
  );
}

ReviewCard.AuthorControls = AuthorControls;

export default ReviewCard;
