import useGetMany from '@/hooks/api/useGetMany';
import usePost from '@/hooks/api/usePost';
import useDelete from '@/hooks/api/useDelete';
import usePut from '@/hooks/api/usePut';
import useSessionStorage from '@/hooks/useSessionStorage';

import { ENDPOINTS } from '@/constants/api';

type PropsWithoutProductId = { size: number };
type PropsWithProductId = PropsWithoutProductId & { productId: number };
type Props = {
  size: number;
  productId?: number;
  reviewId?: number;
};

type ReturnTypeWithoutProductId = [Review[], () => void, () => void];
type ReturnTypeWithProductId = [
  ...ReturnTypeWithoutProductId,
  (reviewInput: ReviewInput) => Promise<void>,
  (id: number) => Promise<void>,
  (reviewInput: ReviewInput, id: number) => Promise<void>
];
type ReturnType = [
  Review[],
  () => void,
  () => void,
  ((reviewInput: ReviewInput) => Promise<void>)?,
  ((id: number) => Promise<void>)?,
  ((reviewInput: ReviewInput, id: number) => Promise<void>)?
];

function useReviews({
  size,
}: PropsWithoutProductId): ReturnTypeWithoutProductId;
function useReviews({
  size,
  productId,
}: PropsWithProductId): ReturnTypeWithProductId;
function useReviews({ size, productId }: Props): ReturnType {
  const [data] = useSessionStorage<UserData>('userData');
  const [reviews, getNextPage, refetch] = useGetMany<Review>({
    url:
      productId !== undefined
        ? `${ENDPOINTS.REVIEWS_BY_PRODUCT_ID(productId)}`
        : `${ENDPOINTS.REVIEWS}`,
    size,
    sort: 'createdAt,desc',
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

  return [
    reviews,
    getNextPage,
    refetch,
    productId !== undefined && postReview,
    deleteReview,
    putReview,
  ];
}

export default useReviews;
