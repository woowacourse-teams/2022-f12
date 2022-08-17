import InfiniteScroll from '@/components/common/InfiniteScroll/InfiniteScroll';
import NoDataPlaceholder from '@/components/common/NoDataPlaceholder/NoDataPlaceholder';

import ProfileCard from '@/components/Profile/ProfileCard/ProfileCard';
import * as S from '@/components/Profile/ProfileSearchResult/ProfileSearchResult.style';
import { PROFILE_SEARCH_SIZE } from '@/pages/ProfileSearch/ProfileSearch';

type Props = Omit<DataFetchStatus, 'isReady'> & {
  data: ProfileSearchResult[];
  getNextPage: () => void;
};

function ProfileSearchResult({
  data: profileSearchData,
  getNextPage,
  isLoading,
  isError,
}: Props) {
  const profileSearchDataList = profileSearchData.map(
    ({ id, gitHubId, imageUrl, careerLevel, jobType, profileProducts }, index) => {
      return (
        <S.CardWrapper key={id}>
          <ProfileCard
            id={id}
            gitHubId={gitHubId}
            imageUrl={imageUrl}
            careerLevel={careerLevel}
            jobType={jobType}
            profileProducts={profileProducts}
            index={index % PROFILE_SEARCH_SIZE}
          />
        </S.CardWrapper>
      );
    }
  );

  return (
    <InfiniteScroll
      handleContentLoad={getNextPage}
      isLoading={isLoading}
      isError={isError}
    >
      <S.Container>{profileSearchDataList}</S.Container>
      {profileSearchData.length === 0 && <NoDataPlaceholder />}
    </InfiniteScroll>
  );
}

export default ProfileSearchResult;
