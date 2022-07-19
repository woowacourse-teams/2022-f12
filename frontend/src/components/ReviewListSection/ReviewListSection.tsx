import InfiniteScroll from '../common/InfiniteScroll/InfiniteScroll';
import ReviewCard from '../common/ReviewCard/ReviewCard';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';
import * as S from './ReviewListSection.style';
import useSessionStorage from '@/hooks/useSessionStorage';

type Props = {
  columns: number;
  data: Review[];
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
}: Props) {
  const [data] = useSessionStorage<UserData>('userData');
  const loginUserGithubId = data?.member.githubId;

  const reviewCardList = reviewData.map(
    ({ id, author, product, content, rating }) => (
      <ReviewCard
        key={id}
        reviewId={id}
        product={product}
        profileImage={author.imageUrl}
        username={author.githubId}
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
        <InfiniteScroll handleContentLoad={getNextPage}>
          {reviewCardList}
        </InfiniteScroll>
      </S.Wrapper>
    </S.Container>
  );
}

export default ReviewListSection;
