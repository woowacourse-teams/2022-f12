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
  productVisible?: boolean;
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
  productVisible,
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
          {reviewData.map((reviewData, index) => {
            const shouldRenderAuthorControls =
              reviewData.authorMatch && !reviewData.product;
            return (
              <ReviewCard
                key={reviewData.id}
                reviewData={reviewData}
                index={index % pageSize}
                productVisible={reviewData.product && productVisible}
                userNameVisible={userNameVisible}
              >
                {shouldRenderAuthorControls && (
                  <ReviewCard.AuthorControls
                    handleFocus={handleFocus}
                    handleDelete={handleDelete}
                    handleEdit={handleEdit}
                  />
                )}
              </ReviewCard>
            );
          })}
        </S.Wrapper>
      </InfiniteScroll>
    </S.Container>
  );
}

export default ReviewListSection;
