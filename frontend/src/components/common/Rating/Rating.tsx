import * as S from './Rating.style';

type Props = {
  rating: number;
};

function Rating({ rating }: Props) {
  return (
    <S.Container>
      <S.Unit>💖</S.Unit>
      <S.Value>{rating}</S.Value>
    </S.Container>
  );
}

export default Rating;
