import Rating from '@/components/common/Rating/Rating';
import ReviewCount from '@/components/common/ReviewCount/ReviewCount';

import * as S from '@/components/Product/ProductCard/ProductCard.style';

type Props = Omit<Product, 'id' | 'category'>;

function ProductCard({ imageUrl, name, rating, reviewCount }: Props) {
  return (
    <S.Container aria-label={name}>
      <S.ImageWrapper>
        <S.Image src={imageUrl} />
      </S.ImageWrapper>
      <S.Name>{name}</S.Name>
      <S.BottomWrapper>
        <Rating rating={rating} />
        <ReviewCount reviewCount={reviewCount} />
      </S.BottomWrapper>
    </S.Container>
  );
}

export default ProductCard;
