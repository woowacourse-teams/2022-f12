import ReviewCard from '@/components/common/ReviewCard/ReviewCard';

type Props = {
  data: Review[];
  handleDelete: (id: number) => void;
  handleEdit: (reviewInput: ReviewInput, id: number) => Promise<void>;
  loginUserGithubId: string;
};

function ReviewList({ data, handleDelete, handleEdit, loginUserGithubId }: Props) {
  return (
    <>
      {data.map(({ id, author, product, content, rating, createdAt }) => (
        <ReviewCard
          key={id}
          reviewId={id}
          product={product}
          author={author}
          rating={rating}
          createdAt={createdAt}
          content={content}
          loginUserGithubId={loginUserGithubId}
          handleDelete={handleDelete}
          handleEdit={handleEdit}
        />
      ))}
    </>
  );
}

export default ReviewList;
