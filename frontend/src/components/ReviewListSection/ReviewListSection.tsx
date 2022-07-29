import InfiniteScroll from '@/components/common/InfiniteScroll/InfiniteScroll';
import ReviewCard from '@/components/common/ReviewCard/ReviewCard';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';
import * as S from '@/components/ReviewListSection/ReviewListSection.style';
import { useContext } from 'react';
import { UserDataContext } from '@/contexts/LoginContextProvider';
import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import Loading from '@/components/common/Loading/Loading';

type Props = {
  columns: number;
  data: Review[];
  isLoading: boolean;
  isReady: boolean;
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
}: Props) {
  const userData = useContext(UserDataContext);
  const loginUserGithubId = userData?.member.gitHubId;

  const reviewCardList = reviewData.map(
    ({ id, author, product, content, rating }) => (
      <ReviewCard
        key={id}
        reviewId={id}
        product={product}
        profileImage={author.imageUrl}
        username={author.gitHubId}
        rating={rating}
        content={content}
        loginUserGithubId={loginUserGithubId}
        handleDelete={handleDelete}
        handleEdit={handleEdit}
      />
    )
  );
  return (
    <S.Container>
      <SectionHeader>
        <S.Title>최근 후기</S.Title>
      </SectionHeader>
      <S.Wrapper columns={columns}>
        <AsyncWrapper fallback={<Loading />} isReady={isReady}>
          <InfiniteScroll handleContentLoad={getNextPage} isLoading={isLoading}>
            {reviewCardList}
          </InfiniteScroll>
        </AsyncWrapper>
      </S.Wrapper>
    </S.Container>
  );
}

export default ReviewListSection;
