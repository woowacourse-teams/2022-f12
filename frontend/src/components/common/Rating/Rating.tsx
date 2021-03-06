import theme from '@/style/theme';
import Heart from '@/assets/heart.svg';
import * as S from './Rating.style';

export type Props = {
  rating: number;
  type?: '일반' | '정수';
  size?: 'small' | 'medium' | 'large';
};

function Rating({ rating, type = '일반', size = 'small' }: Props) {
  return type === '일반' ? (
    <S.Container>
      <S.Unit size={size}>
        <Heart fill={theme.colors.primary} stroke={theme.colors.primaryDark} />
      </S.Unit>
      <S.Value size={size}>{rating.toFixed(2)}</S.Value>
    </S.Container>
  ) : (
    <S.Container>
      {Array.from({ length: rating }).map((_, index) => (
        <S.Unit key={index} size={size}>
          <Heart
            fill={theme.colors.primary}
            stroke={theme.colors.primaryDark}
          />
        </S.Unit>
      ))}
    </S.Container>
  );
}

export default Rating;
