import Rating from '../Rating/Rating';
import UserNameTag from '../UserNameTag/UserNameTag';

import * as S from './ReviewCard.style';

type Props = {
  profileImage: string;
  product?: {
    id: number;
    name: string;
    imageUrl: string;
  };
  username: string;
  rating: number;
  content: string;
  loginUserGithubId: string;
  handleDeleteReview: () => void;
};

function ReviewCard({
  profileImage,
  product,
  username,
  rating,
  content,
  loginUserGithubId,
  handleDeleteReview,
}: Props) {
  return (
    <S.Container>
      {product && (
        <S.ProductArea>
          <S.ImageWrapper>
            <S.Image src={product.imageUrl} />
          </S.ImageWrapper>
          <S.Title>{product.name}</S.Title>
        </S.ProductArea>
      )}
      <S.ReviewArea isFull={!product}>
        <S.Wrapper>
          <S.UserWrapper>
            <UserNameTag profileImage={profileImage} username={username} />
            {!product && loginUserGithubId === username && (
              <>
                <S.EditButton>수정</S.EditButton>
                <S.DeleteButton onClick={handleDeleteReview}>
                  삭제
                </S.DeleteButton>
              </>
            )}
          </S.UserWrapper>
          <Rating type="정수" rating={rating} />
        </S.Wrapper>
        <S.Content>{content}</S.Content>
      </S.ReviewArea>
    </S.Container>
  );
}

export default ReviewCard;
