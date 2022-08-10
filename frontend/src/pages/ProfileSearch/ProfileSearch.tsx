import { useState } from 'react';

import SearchBar from '@/components/common/SearchBar/SearchBar';
import SearchFilter from '@/components/SearchFilter/SearchFilter';

import * as S from '@/pages/ProfileSearch/ProfileSearch.style';
import ProfileSearchResult from '@/components/ProfileSearchResult/ProfileSearchResult';
import Loading from '@/components/common/Loading/Loading';
import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import useSearch from '@/hooks/useSearch';
import { ENDPOINTS } from '@/constants/api';

function ProfileSearch() {
  const [careerLevelFilter, setCareerLevelFilter] = useState('');
  const [jobTypeFilter, setJobTypeFilter] = useState('');
  const [searchInput, setSearchInput] = useState('');

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

  const {
    result: profiles,
    getNextPage,
    isError: isProfileSearchError,
    isLoading: isProfileSearchLoading,
    isReady: isProfileSearchReady,
  } = useSearch<ProfileSearchResult>({
    url: ENDPOINTS.MEMBERS,
    query: searchInput,
    filter: {
      career: careerLevelFilter,
      jobType: jobTypeFilter,
    },
    size: '4',
  });

  const handleCareerLevelFilterClick: React.MouseEventHandler<
    HTMLButtonElement
  > = (e) => {
    if (!(e.target instanceof HTMLButtonElement)) return;
    if (e.target.value === careerLevelFilter) {
      setCareerLevelFilter('');
      return;
    }
    setCareerLevelFilter(e.target.value);
  };

  const handleJobTypeFilterClick: React.MouseEventHandler<HTMLButtonElement> = (
    e
  ) => {
    if (!(e.target instanceof HTMLButtonElement)) return;
    if (e.target.value === jobTypeFilter) {
      setJobTypeFilter('');
      return;
    }
    setJobTypeFilter(e.target.value);
  };

  return (
    <S.Container>
      <S.SearchWrapper>
        <SearchBar searchInput={searchInput} setSearchInput={setSearchInput} />
        <S.SearchFilterWrapper>
          <SearchFilter
            title={'경력'}
            value={careerLevelFilter}
            handleValueClick={handleCareerLevelFilterClick}
            options={careerLevels}
          />
          <SearchFilter
            title={'직군'}
            value={jobTypeFilter}
            handleValueClick={handleJobTypeFilterClick}
            options={jobTypes}
          />
        </S.SearchFilterWrapper>
      </S.SearchWrapper>
      <AsyncWrapper
        fallback={<Loading />}
        isReady={isProfileSearchReady}
        isError={isProfileSearchError}
      >
        <ProfileSearchResult
          data={profiles}
          getNextPage={getNextPage}
          isLoading={isProfileSearchLoading}
          isReady={isProfileSearchReady}
          isError={isProfileSearchError}
        />
      </AsyncWrapper>
    </S.Container>
  );
}

export default ProfileSearch;
