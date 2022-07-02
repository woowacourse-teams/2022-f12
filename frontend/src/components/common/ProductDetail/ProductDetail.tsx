import Rating from '../Rating/Rating';
import * as S from './ProductDetail.style';

type Props = {
  productImage?: string;
  name: string;
  rating: number;
};

function ProductDetail({ productImage, name, rating }: Props) {
  return (
    <S.Container>
      <S.Image src={productImage} />
      <S.Wrapper>
        <S.Name>{name}</S.Name>
        <Rating size="large" rating={rating} />
      </S.Wrapper>
    </S.Container>
  );
}

export default ProductDetail;
