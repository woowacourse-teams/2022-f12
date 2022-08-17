import useDelete from '@/hooks/api/useDelete';
import useGetMany from '@/hooks/api/useGetMany';
import usePost from '@/hooks/api/usePost';
import usePut from '@/hooks/api/usePut';
import useModal from '@/hooks/useModal';
import useSessionStorage from '@/hooks/useSessionStorage';

import { ENDPOINTS } from '@/constants/api';
import { SUCCESS_MESSAGES, CONFIRM_MESSAGES } from '@/constants/messages';

type PropsWithoutProductId = { size: string };
type PropsWithProductId = PropsWithoutProductId & {
  productId: Product['id'];
  handleRefetchOnSuccess?: () => void;
};
type Props = {
  size: string;
  productId?: Product['id'];
  reviewId?: number;
  handleRefetchOnSuccess?: () => void;
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
function useReviews({
  size,
  productId,
  handleRefetchOnSuccess,
}: PropsWithProductId): ReturnWithProductId;
function useReviews({ size, productId, handleRefetchOnSuccess }: Props): Return {
  const [data] = useSessionStorage<UserData>('userData');
  const { showAlert, getConfirm } = useModal();

  const hasToken = data && data.token !== undefined;

  const CommonParams = {
    params: {
      size,
      sort: 'createdAt,desc',
    },
  };

  const ParamsWithProduct = {
    ...CommonParams,
    url: ENDPOINTS.REVIEWS_BY_PRODUCT_ID(productId),
    headers: hasToken ? { Authorization: `Bearer ${data.token}` } : null,
  };

  const ParamsWithoutProduct = {
    ...CommonParams,
    url: ENDPOINTS.REVIEWS,
  };

  const {
    data: reviews,
    getNextPage,
    refetch,
    isReady,
    isLoading,
    isError,
  } = useGetMany<Review>(
    productId === undefined ? ParamsWithoutProduct : ParamsWithProduct
  );

  const postReview = usePost<ReviewInput>({
    url: `${ENDPOINTS.REVIEWS_BY_PRODUCT_ID(productId)}`,
  });

  const deleteReview = useDelete({ url: `${ENDPOINTS.REVIEWS}` });

  const putReview = usePut<ReviewInput>({ url: `${ENDPOINTS.REVIEWS}` });

  const scrollReviewListToTop = () => {
    window.scrollTo({
      top: 0,
      behavior: 'smooth',
    });
  };

  const handleRequestSuccess = async (message: string) => {
    await showAlert(message);

    refetch();
    handleRefetchOnSuccess();
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
