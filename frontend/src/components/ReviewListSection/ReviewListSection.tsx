import InfiniteScroll from '../common/InfiniteScroll/InfiniteScroll';
import ReviewCard from '../common/ReviewCard/ReviewCard';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';
import * as S from './ReviewListSection.style';
import useSessionStorage from '@/hooks/useSessionStorage';

type Props = {
  columns: number;
  data: Review[];
  getNextPage: () => void;
};

const handleDeleteReview = () => {
  console.log('삭제됨');
};

function ReviewListSection({ columns, data: reviewData, getNextPage }: Props) {
  const [data] = useSessionStorage<UserData>('userData');
  const loginUserGithubId = data.member.githubId;

  const reviewCardList = reviewData.map(
    ({ id, author, product, content, rating }) => (
      <ReviewCard
        key={id}
        product={product}
        profileImage={author.imageUrl}
        username={author.githubId}
        rating={rating}
        content={content}
        loginUserGithubId={loginUserGithubId}
        handleDeleteReview={handleDeleteReview}
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
