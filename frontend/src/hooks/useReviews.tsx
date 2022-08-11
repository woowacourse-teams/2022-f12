import useDelete from '@/hooks/api/useDelete';
import useGetMany from '@/hooks/api/useGetMany';
import usePost from '@/hooks/api/usePost';
import usePut from '@/hooks/api/usePut';
import useSessionStorage from '@/hooks/useSessionStorage';

import { ENDPOINTS } from '@/constants/api';

type PropsWithoutProductId = { size: string };
type PropsWithProductId = PropsWithoutProductId & { productId: number };
type Props = {
  size: string;
  productId?: number;
  reviewId?: number;
};

type ReturnTypeWithoutProductId = {
  reviews: Review[];
  isReady: boolean;
  isLoading: boolean;
  isError: boolean;
  getNextPage: () => void;
  refetch: () => void;
};
type ReturnTypeWithProductId = ReturnTypeWithoutProductId & {
  postReview: (reviewInput: ReviewInput) => Promise<void>;
  deleteReview: (id: number) => Promise<void>;
  putReview: (reviewInput: ReviewInput, id: number) => Promise<void>;
};
type ReturnType = ReturnTypeWithoutProductId & ReturnTypeWithProductId;

function useReviews({
  size,
}: PropsWithoutProductId): ReturnTypeWithoutProductId;
function useReviews({
  size,
  productId,
}: PropsWithProductId): ReturnTypeWithProductId;
function useReviews({ size, productId }: Props): ReturnType {
  const [data] = useSessionStorage<UserData>('userData');
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

  return {
    reviews,
    isReady,
    isLoading,
    isError,
    getNextPage,
    refetch,
    postReview,
    deleteReview,
    putReview,
  };
}

export default useReviews;
