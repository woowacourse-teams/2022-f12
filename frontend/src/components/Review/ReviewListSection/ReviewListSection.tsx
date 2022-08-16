import { useContext } from 'react';

import InfiniteScroll from '@/components/common/InfiniteScroll/InfiniteScroll';

import ReviewCard from '@/components/Review/ReviewCard/ReviewCard';
import * as S from '@/components/Review/ReviewListSection/ReviewListSection.style';

import { UserDataContext } from '@/contexts/LoginContextProvider';

type Props = Omit<DataFetchStatus, 'isReady'> & {
  columns: number;
  data: Review[];
  getNextPage: () => void;
  handleDelete?: (id: number) => void;
  handleEdit?: (reviewInput: ReviewInput, id: number) => Promise<void>;
  animationTrigger?: boolean;
};

function ReviewListSection({
  columns,
  data: reviewData,
  getNextPage,
  handleDelete,
  handleEdit,
  isLoading,
  isError,
  animationTrigger,
}: Props) {
  const userData = useContext(UserDataContext);
  const loginUserGithubId = userData?.member.gitHubId;

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
              loginUserGithubId={loginUserGithubId}
              handleDelete={handleDelete}
              handleEdit={handleEdit}
              animationTrigger={animationTrigger}
              index={index}
            />
          ))}
        </S.Wrapper>
      </InfiniteScroll>
    </S.Container>
  );
}

export default ReviewListSection;
