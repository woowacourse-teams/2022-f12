import { useContext } from 'react';

import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import InfiniteScroll from '@/components/common/InfiniteScroll/InfiniteScroll';
import Loading from '@/components/common/Loading/Loading';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';

import ReviewList from '@/components/ReviewList/ReviewList';
import * as S from '@/components/ReviewListSection/ReviewListSection.style';

import { UserDataContext } from '@/contexts/LoginContextProvider';

type Props = {
  columns: number;
  data: Review[];
  isLoading: boolean;
  isReady: boolean;
  isError: boolean;
  getNextPage: () => void;
  handleDelete?: (id: number) => void;
  handleEdit?: (reviewInput: ReviewInput, id: number) => Promise<void>;
};

function ReviewListSection({
  columns,
  data: reviewData,
  getNextPage,
  handleDelete,
  handleEdit,
  isLoading,
  isReady,
  isError,
}: Props) {
  const userData = useContext(UserDataContext);
  const loginUserGithubId = userData?.member.gitHubId;

  return (
    <S.Container aria-label="최근 후기">
      <AsyncWrapper fallback={<Loading />} isReady={isReady} isError={isError}>
        <InfiniteScroll
          handleContentLoad={getNextPage}
          isLoading={isLoading}
          isError={isError}
        >
          <S.Wrapper columns={columns}>
            <ReviewList
              data={reviewData}
              handleDelete={handleDelete}
              handleEdit={handleEdit}
              loginUserGithubId={loginUserGithubId}
            />
          </S.Wrapper>
        </InfiniteScroll>
      </AsyncWrapper>
    </S.Container>
  );
}

export default ReviewListSection;
