import BottomSheet from '@/components/common/BottomSheet/BottomSheet';

import * as S from '@/components/Review/ReviewBottomSheet/ReviewBottomSheet.style';
import ReviewForm from '@/components/Review/ReviewForm/ReviewForm';

type Props = Partial<Pick<Review, 'id' | 'rating' | 'content'>> & {
  handleClose: () => void;
  handleSubmit?: (reviewInput: ReviewInput) => Promise<void>;
  handleEdit?: (reviewInput: ReviewInput, id: number) => Promise<void>;
  isEdit: boolean;
  handleUnmount?: () => void;
  handleFocus?: () => void;
  animationTrigger?: boolean;
};

function ReviewBottomSheet({
  handleClose,
  handleSubmit,
  handleEdit,
  isEdit = false,
  id,
  rating,
  content,
  handleUnmount,
  handleFocus,
  animationTrigger,
}: Props) {
  const handleCloseWithSubmit = async (reviewInput: ReviewInput) => {
    try {
      if (isEdit) {
        await handleEdit(reviewInput, id);
      } else {
        await handleSubmit(reviewInput);
      }
      handleClose();
      handleFocus();
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <BottomSheet
      handleClose={handleClose}
      handleUnmount={handleUnmount}
      animationTrigger={animationTrigger}
    >
      <ReviewForm
        handleSubmit={handleCloseWithSubmit}
        isEdit={isEdit ? true : false}
        rating={rating}
        content={content}
      />
      <S.Button onClick={handleClose}>닫기</S.Button>
    </BottomSheet>
  );
}

export default ReviewBottomSheet;
