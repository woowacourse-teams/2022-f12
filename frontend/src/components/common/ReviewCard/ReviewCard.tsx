import Rating from '../Rating/Rating';
import UserNameTag from '../UserNameTag/UserNameTag';
import useAuth from '@/hooks/useAuth';

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
  reviewId: number;
  handleDelete?: (id: number) => void;
};

function ReviewCard({
  profileImage,
  reviewId,
  product,
  username,
  rating,
  content,
  loginUserGithubId,
  handleDelete,
}: Props) {
  const { isLoggedIn } = useAuth();

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
            {!product && loginUserGithubId === username && isLoggedIn && (
              <>
                <S.EditButton>수정</S.EditButton>
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
    </S.Container>
  );
}

export default ReviewCard;
