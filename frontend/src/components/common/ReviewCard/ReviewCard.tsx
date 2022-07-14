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
};

function ReviewCard({
  profileImage,
  product,
  username,
  rating,
  content,
}: Props) {
  return (
    // {product && '상품정보 넣어주세요~~~!'}
    <S.OuterContainer>
      {product && (
        <S.ProductArea>
          <S.FlexColumnWrapper>
            <S.Image src={product.imageUrl} />
            <S.Title>{product.name}</S.Title>
          </S.FlexColumnWrapper>
        </S.ProductArea>
      )}
      <S.ReviewArea>
        <S.Wrapper>
          <UserNameTag profileImage={profileImage} username={username} />
          <Rating rating={rating} />
        </S.Wrapper>
        <S.Content>{content}</S.Content>
      </S.ReviewArea>
    </S.OuterContainer>
  );
}

export default ReviewCard;
