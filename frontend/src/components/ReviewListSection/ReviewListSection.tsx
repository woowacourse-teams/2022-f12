import { useContext } from 'react';

import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import InfiniteScroll from '@/components/common/InfiniteScroll/InfiniteScroll';
import Loading from '@/components/common/Loading/Loading';
import ReviewCard from '@/components/common/ReviewCard/ReviewCard';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';

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
  handleEdit?: (reviewInput: ReviewInput, id: number) => void;
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
      <SectionHeader>
        <S.Title aria-label="최근 후기">최근 후기</S.Title>
      </SectionHeader>
      <AsyncWrapper fallback={<Loading />} isReady={isReady} isError={isError}>
        <InfiniteScroll
          handleContentLoad={getNextPage}
          isLoading={isLoading}
          isError={isError}
        >
          <S.Wrapper columns={columns}>
            <ReviewCardList
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

const ReviewCardList = ({
  data,
  handleDelete,
  handleEdit,
  loginUserGithubId,
}: Pick<Props, 'data' | 'handleDelete' | 'handleEdit'> & {
  loginUserGithubId: string;
}) => (
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

export default ReviewListSection;
