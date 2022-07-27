import React, { useState } from 'react';

import Heart from '@/assets/heart.svg';

import * as S from '@/components/common/RatingInput/RatingInput.style';
import theme from '@/style/theme';

type Props = {
  rating: null | number;
  setRating: React.Dispatch<React.SetStateAction<number>>;
};

const MAX_RATING = 5;

function RatingInput({ rating = null, setRating }: Props) {
  const [hoverRating, setHoverRating] = useState<null | number>(null);

  const handleClick: (ratingIndex: number) => React.MouseEventHandler =
    (ratingIndex) => () => {
      setRating(ratingIndex);
    };

  const handleHover = (ratingIndex: number) => () => {
    setHoverRating(ratingIndex);
  };

  const resetHover = () => {
    setHoverRating(null);
  };

  return (
    <S.Container>
      {Array.from({ length: MAX_RATING }).map((_, index) => {
        const ratingIndex = index + 1;
        return (
          <S.EmptyButton
            key={ratingIndex}
            type={'button'}
            onMouseUp={handleClick(ratingIndex)}
            onMouseEnter={handleHover(ratingIndex)}
            onMouseLeave={resetHover}
          >
            {ratingIndex > (hoverRating ?? rating) ? (
              <Heart stroke={theme.colors.primaryDark} />
            ) : (
              <Heart
                stroke={theme.colors.primaryDark}
                fill={theme.colors.primary}
              />
            )}
          </S.EmptyButton>
        );
      })}
    </S.Container>
  );
}

export default RatingInput;
