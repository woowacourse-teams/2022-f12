import useGetMany from './api/useGetMany';
import usePost from './api/usePost';
import { BASE_URL, ENDPOINTS } from '../constants/api';

type Reviews = {
  id: number;
  profileImage: string;
  username: string;
  rating: number;
  content: string;
};

type Props = {
  size: number;
  productId?: number;
};

type PropsWithoutProductId = {
  size: number;
};

type PropsWithProductId = {
  size: number;
  productId: number;
};

type ReviewInput = {
  content: string;
  rating: number;
};

type ReturnTypeWithoutProductId = [Reviews[], () => void];

type ReturnTypeWithProductId = [
  Reviews[],
  () => void,
  (reviewInput: ReviewInput) => Promise<void>
];

type ReturnType = [
  Reviews[],
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
  const [reviews, getNextPage] = useGetMany<Reviews>({
    url:
      productId !== undefined
        ? `${BASE_URL}${ENDPOINTS.REVIEWS_BY_PRODUCT_ID(productId)}`
        : `${BASE_URL}${ENDPOINTS.REVIEWS}`,
    size,
  });

  const postReview = usePost<ReviewInput>({
    url: `${BASE_URL}${ENDPOINTS.REVIEWS_BY_PRODUCT_ID(productId)}`,
  });

  return [reviews, getNextPage, productId !== undefined && postReview];
}

export default useReviews;
