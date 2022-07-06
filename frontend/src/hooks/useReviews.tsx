import useGetMany from './api/useGetMany';
import usePost from './api/usePost';

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
        ? `/api/v1/keyboards/${productId}/reviews`
        : '/api/v1/reviews/',
    size,
  });

  const postReview = usePost<ReviewInput>({
    url: '/api/v1/keyboards/1/reviews',
  });

  return [reviews, getNextPage, productId !== undefined && postReview];
}

export default useReviews;
