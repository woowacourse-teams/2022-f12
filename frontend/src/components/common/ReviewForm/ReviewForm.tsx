import * as S from './ReviewForm.style';

function ReviewForm() {
  return (
    <S.Container>
      <S.Title>리뷰 작성하기</S.Title>
      <S.Form>
        <S.Textarea />
        <S.SubmitButton>리뷰 추가</S.SubmitButton>
      </S.Form>
    </S.Container>
  );
}

export default ReviewForm;
