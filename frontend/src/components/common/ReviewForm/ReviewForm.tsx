import React, { useCallback, useState } from 'react';
import RatingInput from '@/components/common/RatingInput/RatingInput';
import * as S from '@/components/common/ReviewForm/ReviewForm.style';

type Props = {
  handleSubmit: (reviewInput: ReviewInput) => Promise<void>;
  isEdit: boolean;
  rating?: number;
  content?: string;
};

const initialState = {
  content: '',
  rating: 0,
};

function ReviewForm({
  handleSubmit,
  isEdit,
  rating: savedRating,
  content: savedContent,
}: Props) {
  const [content, setContent] = useState(
    savedContent ? savedContent : initialState.content
  );
  const [rating, setRating] = useState(
    savedRating ? savedRating : initialState.rating
  );
  const [isFormInvalid, setInvalid] = useState(false);

  const validateReviewInput = (contentInput: string, ratingInput: number) => {
    return !!contentInput && ratingInput !== 0 && contentInput.length <= 1000;
  };

  const resetForm = () => {
    setContent(initialState.content);
    setRating(initialState.rating);
  };

  const handleContentChange: React.ChangeEventHandler<HTMLTextAreaElement> =
    useCallback(({ target: { value } }) => {
      setContent(value);
    }, []);

  const handleFormChange: React.ChangeEventHandler<HTMLFormElement> = () => {
    setInvalid(false);
  };

  const submitForm: React.FormEventHandler<HTMLFormElement> = (e) => {
    e.preventDefault();
    if (!validateReviewInput(content.trim(), rating)) {
      setInvalid(true);
      throw Error('항목이 누락됨');
    }

    handleSubmit({ content, rating })
      .then(() => {
        resetForm();
      })
      .catch((error) => {
        throw error;
      });
  };

  const isRatingEmpty = rating === 0;
  const isContentEmpty = content.trim() === '';

  return (
    <S.Container>
      <S.Title>{isEdit ? '리뷰 수정하기' : '리뷰 작성하기'}</S.Title>
      <S.Form onSubmit={submitForm} onChange={handleFormChange}>
        <S.Label isInvalid={isFormInvalid && isRatingEmpty}>
          <p>평점을 입력해주세요</p>
          <RatingInput rating={rating} setRating={setRating} />
        </S.Label>
        <S.Label isInvalid={isFormInvalid && isContentEmpty}>
          <S.LabelTop>
            <p>총평을 입력해주세요</p>
            <p>{content.length} / 1000</p>
          </S.LabelTop>
          <S.Textarea value={content} onChange={handleContentChange} required />
        </S.Label>
        <S.Footer>
          <S.SubmitButton>{isEdit ? '리뷰 수정' : '리뷰 추가'}</S.SubmitButton>
          {isFormInvalid && (
            <S.ErrorMessage>모든 항목을 입력해주세요</S.ErrorMessage>
          )}
        </S.Footer>
      </S.Form>
    </S.Container>
  );
}

export default ReviewForm;
