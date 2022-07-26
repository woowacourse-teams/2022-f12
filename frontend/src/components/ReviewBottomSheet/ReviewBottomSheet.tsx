import BottomSheet from '@/components/common/BottomSheet/BottomSheet';
import ReviewForm from '@/components/common/ReviewForm/ReviewForm';
import * as S from '@/components/ReviewBottomSheet/ReviewBottomSheet.style';

type Props = {
  handleClose: () => void;
  handleSubmit?: (reviewInput: ReviewInput) => Promise<void>;
  handleEdit?: (reviewInput: ReviewInput, id: number) => void;
  isEdit: boolean;
  reviewId?: number;
  rating?: number;
  content?: string;
};

function ReviewBottomSheet({
  handleClose,
  handleSubmit,
  handleEdit,
  isEdit = false,
  reviewId,
  rating,
  content,
}: Props) {
  const handleCloseWithSubmit = async (reviewInput: ReviewInput) => {
    try {
      if (isEdit) {
        handleEdit(reviewInput, reviewId);
      } else {
        await handleSubmit(reviewInput);
      }
      handleClose();
    } catch (error) {
      console.log(error);
    }
  };
  return (
    <BottomSheet handleClose={handleClose}>
      <S.Button onClick={handleClose}>닫기</S.Button>
      <ReviewForm
        handleSubmit={handleCloseWithSubmit}
        isEdit={isEdit ? true : false}
        rating={rating}
        content={content}
      />
    </BottomSheet>
  );
}

export default ReviewBottomSheet;
