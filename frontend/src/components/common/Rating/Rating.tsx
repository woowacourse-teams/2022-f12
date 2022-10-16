import { Fragment } from 'react';

import * as S from '@/components/common/Rating/Rating.style';

import theme from '@/style/theme';

import Heart from '@/assets/heart.svg';

export type Props = {
  rating: number;
  type?: '일반' | '정수';
  size?: 'small' | 'medium' | 'large';
};

function Rating({ rating, type = '일반', size = 'small' }: Props) {
  const RatingUnit = (
    <S.Unit size={size}>
      <Heart fill={theme.colors.primary} stroke={theme.colors.primaryDark} />
    </S.Unit>
  );

  return (
    <S.Container>
      {type === '일반' ? (
        <>
          {RatingUnit}
          <S.Value size={size}>
            <S.UnreadableValue aria-hidden="true">{rating.toFixed(2)}</S.UnreadableValue>
            <S.ReadableValue>{`평점 ${rating.toFixed(2)}점`}</S.ReadableValue>
          </S.Value>
        </>
      ) : (
        Array.from({ length: rating }).map((_, index) => (
          <Fragment key={index}>{RatingUnit}</Fragment>
        ))
      )}
    </S.Container>
  );
}

export default Rating;
