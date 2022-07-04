import { useState } from 'react';
import RatingInput from '../RatingInput/RatingInput';
import * as S from './ReviewForm.style';

function ReviewForm() {
  const [rating, setRating] = useState<number>(0);

  return (
    <S.Container>
      <S.Title>리뷰 작성하기</S.Title>
      <S.Form>
        <RatingInput rating={rating} setRating={setRating} />
        <S.Textarea />
        <S.SubmitButton>리뷰 추가</S.SubmitButton>
      </S.Form>
    </S.Container>
  );
}

export default ReviewForm;
