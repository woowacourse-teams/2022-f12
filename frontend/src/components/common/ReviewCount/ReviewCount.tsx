import * as S from '@/components/common/ReviewCount/ReviewCount.style';

import ReviewIcon from '@/assets/review.svg';

type Props = {
  reviewCount: number;
  size?: 'small' | 'large';
};

function ReviewCount({ reviewCount, size = 'small' }: Props) {
  return (
    <S.Container>
      <S.ReviewIconWrapper size={size}>
        <ReviewIcon />
      </S.ReviewIconWrapper>
      <S.Value size={size}>
        <S.UnreadableValue aria-hidden="true">{reviewCount}</S.UnreadableValue>
        <S.ReadableValue>{`${reviewCount}개의 리뷰`}</S.ReadableValue>
      </S.Value>
    </S.Container>
  );
}

export default ReviewCount;
