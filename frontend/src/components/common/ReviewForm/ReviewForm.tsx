import React, { useState } from 'react';
import RatingInput from '../RatingInput/RatingInput';
import * as S from './ReviewForm.style';

type Props = {
  handleSubmit: (reviewInput: ReviewInput) => Promise<void>;
};

const initialState = {
  content: '',
  rating: 0,
};

function ReviewForm({ handleSubmit }: Props) {
  const [content, setContent] = useState(initialState.content);
  const [rating, setRating] = useState(initialState.rating);

  const validateReviewInput = () => {
    return !!content && rating !== 0 && content.length <= 1000;
  };

  const resetForm = () => {
    setContent(initialState.content);
    setRating(initialState.rating);
  };

  const handleContentChange: React.ChangeEventHandler<HTMLTextAreaElement> = ({
    target: { value },
  }) => {
    setContent(value);
  };

  const submitForm: React.FormEventHandler<HTMLFormElement> = (e) => {
    e.preventDefault();
    if (!validateReviewInput()) {
      alert('모든 항목을 작성해주세요');
    }

    handleSubmit({ content, rating })
      .then(() => {
        resetForm();
      })
      .catch((error) => {
        console.log(error);
      });
  };

  return (
    <S.Container>
      <S.Title>리뷰 작성하기</S.Title>
      <S.Form onSubmit={submitForm}>
        <RatingInput rating={rating} setRating={setRating} />
        <S.Textarea value={content} onChange={handleContentChange} required />
        <S.SubmitButton>리뷰 추가</S.SubmitButton>
      </S.Form>
    </S.Container>
  );
}

export default ReviewForm;
