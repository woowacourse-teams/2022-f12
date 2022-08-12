import InfiniteScroll from '@/components/common/InfiniteScroll/InfiniteScroll';
import NoDataPlaceholder from '@/components/common/NoDataPlaceholder/NoDataPlaceholder';

import ProfileCard from '@/components/ProfileCard/ProfileCard';
import * as S from '@/components/ProfileSearchResult/ProfileSearchResult.style';

type Props = {
  data: ProfileSearchResult[];
  getNextPage: () => void;
  isLoading: boolean;
  isError: boolean;
};

function ProfileSearchResult({
  data: profileSearchData,
  getNextPage,
  isLoading,
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
