import InfiniteScroll from '@/components/common/InfiniteScroll/InfiniteScroll';

import ReviewCard from '@/components/Review/ReviewCard/ReviewCard';
import * as S from '@/components/Review/ReviewListSection/ReviewListSection.style';

type Props = Omit<DataFetchStatus, 'isReady'> & {
  columns: number;
  data: Review[];
  getNextPage: () => void;
  handleDelete?: (id: number) => void;
  handleEdit?: (reviewInput: ReviewInput, id: number) => Promise<void>;
  handleFocus?: () => void;
  pageSize?: number;
  userNameVisible?: boolean;
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
  userNameVisible,
  handleFocus,
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
              handleFocus={handleFocus}
              handleDelete={handleDelete}
              handleEdit={handleEdit}
              index={index % pageSize}
              userNameVisible={userNameVisible}
            />
          ))}
        </S.Wrapper>
      </InfiniteScroll>
    </S.Container>
  );
}

export default ReviewListSection;
