import useDelete from '@/hooks/api/useDelete';
import useGetMany from '@/hooks/api/useGetMany';
import usePost from '@/hooks/api/usePost';
import usePut from '@/hooks/api/usePut';
import useModal from '@/hooks/useModal';
import useSessionStorage from '@/hooks/useSessionStorage';

import { ENDPOINTS } from '@/constants/api';
import { SUCCESS_MESSAGES, CONFIRM_MESSAGES } from '@/constants/messages';

type PropsWithoutProductId = { size: string };
type PropsWithProductId = PropsWithoutProductId & { productId: Product['id'] };
type Props = {
  size: string;
  productId?: Product['id'];
  reviewId?: number;
};

type ReturnWithoutProductId = DataFetchStatus & {
  reviews: Review[];
  getNextPage: () => void;
};
type ReturnWithProductId = ReturnWithoutProductId & {
  handleSubmit: (reviewInput: ReviewInput) => Promise<void>;
  handleEdit: (reviewInput: ReviewInput, id: number) => Promise<void>;
  handleDelete: (id: number) => Promise<void>;
};
type Return = ReturnWithoutProductId & ReturnWithProductId;

function useReviews({ size }: PropsWithoutProductId): ReturnWithoutProductId;
function useReviews({ size, productId }: PropsWithProductId): ReturnWithProductId;
function useReviews({ size, productId }: Props): Return {
  const [data] = useSessionStorage<UserData>('userData');
  const { showAlert, getConfirm } = useModal();
  const {
    data: reviews,
    getNextPage,
    refetch,
    isReady,
    isLoading,
    isError,
  } = useGetMany<Review>({
    url:
      productId !== undefined
        ? `${ENDPOINTS.REVIEWS_BY_PRODUCT_ID(productId)}`
        : `${ENDPOINTS.REVIEWS}`,
    params: {
      size,
      sort: 'createdAt,desc',
    },
  });

  const postReview = usePost<ReviewInput>({
    url: `${ENDPOINTS.REVIEWS_BY_PRODUCT_ID(productId)}`,
    headers: { Authorization: `Bearer ${data?.token}` },
  });

  const deleteReview = useDelete({
    url: `${ENDPOINTS.REVIEWS}`,
    headers: { Authorization: `Bearer ${data?.token}` },
  });

  const putReview = usePut<ReviewInput>({
    url: `${ENDPOINTS.REVIEWS}`,
    headers: { Authorization: `Bearer ${data?.token}` },
  });

  const scrollReviewListToTop = () => {
    window.scrollTo({
      top: 0,
      behavior: 'smooth',
    });
  };

  const handleRequestSuccess = async (message: string) => {
    await showAlert(message);

    refetch();
    scrollReviewListToTop();
  };

  const handleSubmit = async (reviewInput: ReviewInput) => {
    try {
      await postReview(reviewInput);

      await handleRequestSuccess(SUCCESS_MESSAGES.REVIEW_CREATE);
    } catch {
      return;
    }
  };

  const handleEdit = async (reviewInput: ReviewInput, id: number) => {
    try {
      await putReview(reviewInput, id);

      await handleRequestSuccess(SUCCESS_MESSAGES.REVIEW_EDIT);
    } catch {
      return;
    }
  };

  const handleDelete = async (id: number) => {
    const confirmation = await getConfirm(CONFIRM_MESSAGES.REVIEW_DELETE);
    if (!confirmation) return;

    try {
      await deleteReview(id);

      await handleRequestSuccess(SUCCESS_MESSAGES.REVIEW_DELETE);
    } catch {
      return;
    }
  };

  return {
    reviews,
    isReady,
    isLoading,
    isError,
    getNextPage,
    handleSubmit,
    handleEdit,
    handleDelete,
  };
}

export default useReviews;
