import Rating from '../Rating/Rating';
import * as S from './ProductCard.style';
import sampleKeyboard from './sample_keyboard.jpg';

type Props = {
  productImage?: string;
  name: string;
  rating: number;
};

function ProductCard({ productImage = sampleKeyboard, name, rating }: Props) {
  return (
    <S.Container>
      <S.Image src={productImage} />
      <S.Wrapper>
        <S.Name>{name}</S.Name>
        <Rating rating={rating} />
      </S.Wrapper>
    </S.Container>
  );
}

export default ProductCard;
