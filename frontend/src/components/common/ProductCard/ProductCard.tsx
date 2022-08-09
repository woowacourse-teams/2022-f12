import Rating from '@/components/common/Rating/Rating';
import * as S from '@/components/common/ProductCard/ProductCard.style';
import ReviewIcon from '@/assets/review.svg';

type Props = {
  imageUrl?: string;
  name: string;
  rating: number;
  reviewCount: number;
};

function ProductCard({ imageUrl, name, rating, reviewCount }: Props) {
  return (
    <S.Container aria-label={name}>
      <S.ImageWrapper>
        <S.Image src={imageUrl} />
      </S.ImageWrapper>
      <S.Name>{name}</S.Name>
      <S.Details>
        <Rating rating={rating} />
        <S.ReviewCount>
          <S.ReviewIconWrapper>
            <ReviewIcon />
          </S.ReviewIconWrapper>
          {reviewCount}
        </S.ReviewCount>
      </S.Details>
    </S.Container>
  );
}

export default ProductCard;
