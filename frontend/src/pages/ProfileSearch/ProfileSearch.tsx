import * as S from '@/pages/ProfileSearch/ProfileSearch.style';

import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import Loading from '@/components/common/Loading/Loading';
import SearchBar from '@/components/common/SearchBar/SearchBar';

import ProfileSearchResult from '@/components/ProfileSearchResult/ProfileSearchResult';
import SearchFilter from '@/components/SearchFilter/SearchFilter';

import useSearch from '@/hooks/useSearch';
import useUrlSyncState from '@/hooks/useUrlSyncState';

import { ENDPOINTS } from '@/constants/api';

const careerLevels = {
  none: '경력 없음',
  junior: '0-2년차',
  midlevel: '3-5년차',
  senior: '6년차 이상',
} as const;

const jobTypes = {
  frontend: '프론트엔드',
  backend: '백엔드',
  mobile: '모바일',
  etc: '기타',
} as const;

function ProfileSearch() {
  const [careerLevel, setCareerLevel] = useUrlSyncState('careerLevel');
  const [jobType, setJobType] = useUrlSyncState('jobType');
  const [searchInput, setSearchInput] = useUrlSyncState('keyword');

  const {
    result: profiles,
    getNextPage,
    isError,
    isLoading,
    isReady,
  } = useSearch<ProfileSearchResult>({
    url: ENDPOINTS.MEMBERS,
    query: searchInput,
    filter: {
      career: careerLevel,
      jobType: jobType,
    },
    size: '4',
  });

  return (
    <S.Container>
      <S.SearchWrapper>
        <SearchBar searchInput={searchInput} setSearchInput={setSearchInput} />
        <S.SearchFilterWrapper>
          <SearchFilter
            title={'경력'}
            value={careerLevel}
            setValue={setCareerLevel}
            options={careerLevels}
          />
          <SearchFilter
            title={'직군'}
            value={jobType}
            setValue={setJobType}
            options={jobTypes}
          />
        </S.SearchFilterWrapper>
      </S.SearchWrapper>
      <AsyncWrapper fallback={<Loading />} isReady={isReady} isError={isError}>
        <ProfileSearchResult
          data={profiles}
          getNextPage={getNextPage}
          isLoading={isLoading}
          isError={isError}
        />
      </AsyncWrapper>
    </S.Container>
  );
}

export default ProfileSearch;
