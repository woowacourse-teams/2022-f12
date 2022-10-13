import LazyImage from '@/components/common/LazyImage/LazyImage';
import Rating from '@/components/common/Rating/Rating';
import ReviewCount from '@/components/common/ReviewCount/ReviewCount';

import * as S from '@/components/Product/ProductCard/ProductCard.style';

type Props = Omit<Product, 'id' | 'category'> & {
  index?: number;
  size?: 's' | 'm' | 'l';
};

function ProductCard({
  imageUrl,
  name,
  rating,
  reviewCount,
  index = 0,
  size = 'l',
}: Props) {
  return (
    <S.Container aria-label={name} index={index} size={size}>
      <S.ImageWrapper>
        <LazyImage src={imageUrl} alt={''} />
      </S.ImageWrapper>
      <S.Name size={size}>{name}</S.Name>
      <S.BottomWrapper>
        <Rating rating={rating} />
        <ReviewCount reviewCount={reviewCount} />
      </S.BottomWrapper>
    </S.Container>
  );
}

export default ProductCard;
