import Rating from '@/components/common/Rating/Rating';
import * as S from '@/components/common/ProductDetail/ProductDetail.style';

type Props = {
  product: Product;
};

function ProductDetail({ product }: Props) {
  const { imageUrl, name, rating } = product;

  return (
    <S.Container>
      <S.Image src={imageUrl} aria-label="상품 이미지" />
      <S.Wrapper>
        <S.Name>{name}</S.Name>
        <Rating size="large" rating={rating} />
      </S.Wrapper>
    </S.Container>
  );
}

export default ProductDetail;
