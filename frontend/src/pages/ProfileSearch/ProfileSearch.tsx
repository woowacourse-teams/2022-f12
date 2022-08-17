import * as S from '@/pages/ProfileSearch/ProfileSearch.style';

import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import Loading from '@/components/common/Loading/Loading';
import SearchBar from '@/components/common/SearchBar/SearchBar';
import SearchFilter from '@/components/common/SearchFilter/SearchFilter';

import ProfileSearchResult from '@/components/Profile/ProfileSearchResult/ProfileSearchResult';

import useSearch from '@/hooks/useSearch';
import useUrlSyncState from '@/hooks/useUrlSyncState';

import { ENDPOINTS } from '@/constants/api';
import { CAREER_LEVELS, JOB_TYPES } from '@/constants/profile';
import SEARCH_PARAMS from '@/constants/searchParams';

function ProfileSearch() {
  const [careerLevel, setCareerLevel] = useUrlSyncState(SEARCH_PARAMS.CAREER_LEVEL);
  const [jobType, setJobType] = useUrlSyncState(SEARCH_PARAMS.JOB_TYPE);
  const [searchInput, setSearchInput] = useUrlSyncState(SEARCH_PARAMS.KEYWORD);

  const {
    result: profiles,
    getNextPage,
    isError,
    isLoading,
    isReady,
  } = useSearch<ProfileSearchResult>({
    url: ENDPOINTS.MEMBERS,
    query: searchInput,
    filter: { careerLevel, jobType },
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
            options={CAREER_LEVELS}
          />
          <SearchFilter
            title={'직군'}
            value={jobType}
            setValue={setJobType}
            options={JOB_TYPES}
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
