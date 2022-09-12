import LazyImage from '@/components/common/LazyImage/LazyImage';
import Rating from '@/components/common/Rating/Rating';
import ReviewCount from '@/components/common/ReviewCount/ReviewCount';

import * as S from '@/components/Product/ProductCard/ProductCard.style';

type Props = Omit<Product, 'id' | 'category'> & {
  index?: number;
};

function ProductCard({ imageUrl, name, rating, reviewCount, index = 0 }: Props) {
  return (
    <S.Container aria-label={name} index={index}>
      <S.ImageWrapper>
        <LazyImage src={imageUrl} />
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
