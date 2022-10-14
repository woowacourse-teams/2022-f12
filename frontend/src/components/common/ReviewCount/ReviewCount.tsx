import * as S from '@/components/common/ReviewCount/ReviewCount.style';

import ReviewIcon from '@/assets/review.svg';

type Props = {
  reviewCount: number;
  size?: 'small' | 'large';
};

function ReviewCount({ reviewCount, size = 'small' }: Props) {
  return (
    <S.Container aria-label={`리뷰 ${reviewCount}개`}>
      <S.ReviewIconWrapper size={size} aria-hidden={'true'}>
        <ReviewIcon />
      </S.ReviewIconWrapper>
      <S.Value size={size} aria-hidden={'true'}>
        {reviewCount}
      </S.Value>
    </S.Container>
  );
}

export default ReviewCount;
