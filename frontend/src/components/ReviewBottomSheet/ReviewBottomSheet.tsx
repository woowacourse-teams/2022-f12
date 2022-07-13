import BottomSheet from '@/components/common/BottomSheet/BottomSheet';
import ReviewForm from '@/components/common/ReviewForm/ReviewForm';

type Props = {
  handleClose: () => void;
  handleSubmit: () => Promise<void>;
};

function ReviewBottomSheet({ handleClose, handleSubmit }: Props) {
  return (
    <BottomSheet handleClose={handleClose}>
      <ReviewForm handleSubmit={handleSubmit} />
    </BottomSheet>
  );
}

export default ReviewBottomSheet;
