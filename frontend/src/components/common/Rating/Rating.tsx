import Heart from './heart.svg';
import * as S from './Rating.style';

export type Props = {
  rating: number;
  size?: 'small' | 'medium' | 'large';
};

function Rating({ rating, size = 'small' }: Props) {
  return (
    <S.Container>
      <S.Unit size={size}>
        <Heart />
      </S.Unit>
      <S.Value size={size}>{rating.toFixed(2)}</S.Value>
    </S.Container>
  );
}

export default Rating;
