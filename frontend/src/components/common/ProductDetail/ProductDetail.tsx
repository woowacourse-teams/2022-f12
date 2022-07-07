import Rating from '../Rating/Rating';
import * as S from './ProductDetail.style';

type Props = {
  imageUrl?: string;
  name: string;
  rating: number;
};

function ProductDetail({ imageUrl, name, rating }: Props) {
  return (
    <S.Container>
      <S.Image src={imageUrl} />
      <S.Wrapper>
        <S.Name>{name}</S.Name>
        <Rating size="large" rating={rating} />
      </S.Wrapper>
    </S.Container>
  );
}

export default ProductDetail;
