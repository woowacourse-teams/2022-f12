import * as S from '@/components/common/ReviewCount/ReviewCount.style';

import { SROnly } from '@/style/GlobalStyles';

import ReviewIcon from '@/assets/review.svg';

type Props = {
  reviewCount: number;
  size?: 'small' | 'large';
};

function ReviewCount({ reviewCount, size = 'small' }: Props) {
  return (
    <>
      <S.Container aria-hidden={'true'}>
        <S.ReviewIconWrapper size={size}>
          <ReviewIcon />
        </S.ReviewIconWrapper>
        <S.Value size={size}>{reviewCount}</S.Value>
      </S.Container>
      <SROnly>리뷰 {reviewCount}개</SROnly>
    </>
  );
}

export default ReviewCount;
