import InfiniteScroll from '@/components/common/InfiniteScroll/InfiniteScroll';

import ReviewCard from '@/components/Review/ReviewCard/ReviewCard';
import * as S from '@/components/Review/ReviewListSection/ReviewListSection.style';

type Props = Omit<DataFetchStatus, 'isReady'> & {
  columns: number;
  data: Review[];
  getNextPage: () => void;
  handleDelete?: (id: number) => void;
  handleEdit?: (reviewInput: ReviewInput, id: number) => Promise<void>;
  pageSize?: number;
};

function ReviewListSection({
  columns,
  data: reviewData,
  getNextPage,
  handleDelete,
  handleEdit,
  isLoading,
  isError,
  pageSize = 10,
}: Props) {
  return (
    <S.Container aria-label="최근 후기">
      <InfiniteScroll
        handleContentLoad={getNextPage}
        isLoading={isLoading}
        isError={isError}
      >
        <S.Wrapper columns={columns}>
          {reviewData.map(({ id, ...reviewData }, index) => (
            <ReviewCard
              key={id}
              reviewId={id}
              reviewData={reviewData}
              handleDelete={handleDelete}
              handleEdit={handleEdit}
              index={index % pageSize}
            />
          ))}
        </S.Wrapper>
      </InfiniteScroll>
    </S.Container>
  );
}

export default ReviewListSection;
