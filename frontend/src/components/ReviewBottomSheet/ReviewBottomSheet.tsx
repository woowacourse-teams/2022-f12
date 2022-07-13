import BottomSheet from '@/components/common/BottomSheet/BottomSheet';
import ReviewForm from '@/components/common/ReviewForm/ReviewForm';

type Props = {
  handleClose: () => void;
  handleSubmit: (reviewInput: ReviewInput) => Promise<void>;
};

function ReviewBottomSheet({ handleClose, handleSubmit }: Props) {
  const handleCloseWithSubmit = async (reviewInput: ReviewInput) => {
    try {
      await handleSubmit(reviewInput);
      handleClose();
    } catch (error) {
      console.log(error);
    }
  };
  return (
    <BottomSheet handleClose={handleClose}>
      <ReviewForm handleSubmit={handleCloseWithSubmit} />
    </BottomSheet>
  );
}

export default ReviewBottomSheet;
