import Rating from '@/components/common/Rating/Rating';
import ReviewCount from '@/components/common/ReviewCount/ReviewCount';

import * as S from '@/components/Product/ProductDetail/ProductDetail.style';

type Props = {
  product: Product;
};

function ProductDetail({ product }: Props) {
  const { imageUrl, name, rating, reviewCount } = product;

  return (
    <S.Container>
      <S.Image src={imageUrl} aria-label="상품 이미지" />
      <S.Wrapper>
        <S.Name>{name}</S.Name>
        <S.Details>
          <ReviewCount reviewCount={reviewCount} size="large" />
          <Rating size="large" rating={rating} />
        </S.Details>
      </S.Wrapper>
    </S.Container>
  );
}

export default ProductDetail;
