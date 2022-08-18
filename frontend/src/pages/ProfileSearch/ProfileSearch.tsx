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
import useSessionStorage from '@/hooks/useSessionStorage';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';

export const PROFILE_SEARCH_SIZE = 4;

type Props = {
  type?: 'default' | 'following';
};

function ProfileSearch({ type = 'default' }: Props) {
  const [careerLevel, setCareerLevel] = useUrlSyncState(SEARCH_PARAMS.CAREER_LEVEL);
  const [jobType, setJobType] = useUrlSyncState(SEARCH_PARAMS.JOB_TYPE);
  const [searchInput, setSearchInput] = useUrlSyncState(SEARCH_PARAMS.KEYWORD);

  const [userData] = useSessionStorage<UserData>('userData');
  const hasToken = userData && userData.token !== undefined;

  const commonParams = {
    query: searchInput,
    filter: { careerLevel, jobType },
    size: String(PROFILE_SEARCH_SIZE),
  };

  const defaultParams = {
    ...commonParams,
    url: ENDPOINTS.MEMBERS,
  };

  const followingPageParams = {
    ...commonParams,
    url: ENDPOINTS.MY_FOLLOWING,
    headers: hasToken ? { Authorization: `Bearer ${userData.token}` } : null,
  };

  const {
    result: profiles,
    getNextPage,
    isError,
    isLoading,
    isReady,
  } = useSearch<ProfileSearchResult>(
    type === 'default' ? defaultParams : followingPageParams
  );

  return (
    <>
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
      <SectionHeader title={type === 'following' ? '팔로잉' : '프로필 검색'} />
      <AsyncWrapper fallback={<Loading />} isReady={isReady} isError={isError}>
        <ProfileSearchResult
          data={profiles}
          getNextPage={getNextPage}
          isLoading={isLoading}
          isError={isError}
        />
      </AsyncWrapper>
    </>
  );
}

export default ProfileSearch;
