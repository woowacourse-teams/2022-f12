import * as S from '@/components/ProfileSearchResult/ProfileSearchResult.style';
import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import Loading from '@/components/common/Loading/Loading';
import InfiniteScroll from '@/components/common/InfiniteScroll/InfiniteScroll';
import ProfileCard from '@/components/ProfileCard/ProfileCard';

type Props = {
  data: ProfileSearchResult[];
  getNextPage: () => void;
  isLoading: boolean;
  isReady: boolean;
  isError: boolean;
};

function ProfileSearchResult({
  data: profileSearchData,
  getNextPage,
  isLoading,
  isReady,
  isError,
}: Props) {
  const profileSearchDataList = profileSearchData.map(
    ({ id, gitHubId, imageUrl, careerLevel, jobType, profileProducts }) => {
      return (
        <ProfileCard
          id={id}
          key={id}
          gitHubId={gitHubId}
          imageUrl={imageUrl}
          careerLevel={careerLevel}
          jobType={jobType}
          profileProducts={profileProducts}
        />
      );
    }
  );

  return (
    <AsyncWrapper fallback={<Loading />} isReady={isReady} isError={isError}>
      <InfiniteScroll
        handleContentLoad={getNextPage}
        isLoading={isLoading}
        isError={isError}
      >
        <S.Container>{profileSearchDataList}</S.Container>
      </InfiniteScroll>
    </AsyncWrapper>
  );
}

export default ProfileSearchResult;
