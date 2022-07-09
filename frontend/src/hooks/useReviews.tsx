import useGetMany from '@/hooks/api//useGetMany';
import usePost from '@/hooks/api//usePost';
import { ENDPOINTS } from '@/constants/api';

type PropsWithoutProductId = { size: number };
type PropsWithProductId = PropsWithoutProductId & { productId: number };
type Props = {
  size: number;
  productId?: number;
};

type ReturnTypeWithoutProductId = [Review[], () => void];
type ReturnTypeWithProductId = [
  ...ReturnTypeWithoutProductId,
  (reviewInput: ReviewInput) => Promise<void>
];
type ReturnType = [
  Review[],
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
  const [reviews, getNextPage] = useGetMany<Review>({
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

  return [reviews, getNextPage, productId !== undefined && postReview];
}

export default useReviews;
