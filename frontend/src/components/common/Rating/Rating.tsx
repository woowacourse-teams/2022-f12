import Heart from './heart.svg';
import * as S from './Rating.style';

export type Props = {
  rating: number;
};

function Rating({ rating }: Props) {
  return (
    <S.Container>
      <S.Unit>
        <Heart width="14" height="14" />
      </S.Unit>
      <S.Value>{rating.toFixed(2)}</S.Value>
    </S.Container>
  );
}

export default Rating;
