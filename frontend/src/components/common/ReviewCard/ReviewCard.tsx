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
    <S.Container>
      {product && (
        <S.ProductArea>
          <S.ImageWrapper>
            <S.Image src={product.imageUrl} />
          </S.ImageWrapper>
          <S.Title>{product.name}</S.Title>
        </S.ProductArea>
      )}
      <S.ReviewArea>
        <S.Wrapper>
          <UserNameTag profileImage={profileImage} username={username} />
          <Rating type="정수" rating={rating} />
        </S.Wrapper>
        <S.Content>{content}</S.Content>
      </S.ReviewArea>
    </S.Container>
  );
}

export default ReviewCard;
