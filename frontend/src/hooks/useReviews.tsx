import useGetMany from '@/hooks/api//useGetMany';
import usePost from '@/hooks/api//usePost';
import { ENDPOINTS } from '@/constants/api';

type PropsWithoutProductId = { size: number };
type PropsWithProductId = PropsWithoutProductId & { productId: number };
type Props = {
  size: number;
  productId?: number;
};

type ReturnTypeWithoutProductId = [Review[], () => void, () => void];
type ReturnTypeWithProductId = [
  ...ReturnTypeWithoutProductId,
  (reviewInput: ReviewInput) => Promise<void>
];
type ReturnType = [
  Review[],
  () => void,
  () => void,
  ((reviewInput: ReviewInput) => Promise<void>)?
];

function useReviews({
  size,
}: PropsWithoutProductId): ReturnTypeWithoutProductId;
function useReviews({
  size,
  productId,
}: PropsWithProductId): ReturnTypeWithProductId;
function useReviews({ size, productId }: Props): ReturnType {
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
  });

  return [reviews, getNextPage, refetch, productId !== undefined && postReview];
}

export default useReviews;
