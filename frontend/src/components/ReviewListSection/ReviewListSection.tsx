import InfiniteScroll from '../common/InfiniteScroll/InfiniteScroll';
import ReviewCard from '../common/ReviewCard/ReviewCard';
import SectionHeader from '../common/SectionHeader/SectionHeader';
import * as S from './ReviewListSection.style';

type Props = {
  columns: number;
  data: Review[];
  getNextPage: () => void;
};

function ReviewListSection({ columns, data, getNextPage }: Props) {
  const reviewCardList = data.map(
    ({ id, profileImage, username, rating, content }) => (
      <ReviewCard
        key={id}
        profileImage={profileImage}
        username={username}
        rating={rating}
        content={content}
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
