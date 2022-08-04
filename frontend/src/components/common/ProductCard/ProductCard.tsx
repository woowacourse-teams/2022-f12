import Rating from '@/components/common/Rating/Rating';
import * as S from '@/components/common/ProductCard/ProductCard.style';

type Props = {
  imageUrl?: string;
  name: string;
  rating: number;
};

function ProductCard({ imageUrl, name, rating }: Props) {
  return (
    <S.Container aria-label={name}>
      <S.ImageWrapper>
        <S.Image src={imageUrl} />
      </S.ImageWrapper>
      <S.Name>{name}</S.Name>
      <Rating rating={rating} />
    </S.Container>
  );
}

export default ProductCard;
