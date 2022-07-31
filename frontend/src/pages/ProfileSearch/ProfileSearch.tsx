import { useState } from 'react';

import SearchBar from '@/components/common/SearchBar/SearchBar';
import SearchFilter from '@/components/SearchFilter/SearchFilter';
import ProfileCard from '@/components/ProfileCard/ProfileCard';

import * as S from '@/pages/ProfileSearch/ProfileSearch.style';

function ProfileSearch() {
  const [careerLevelFilter, setCareerLevelFilter] = useState('');
  const [jobTypeFilter, setJobTypeFilter] = useState('');

  const handleCareerLevelFilterClick: React.MouseEventHandler<
    HTMLButtonElement
  > = (e) => {
    if (!(e.target instanceof HTMLButtonElement)) return;
    setCareerLevelFilter(e.target.value);
  };

  const handleJobTypeFilterClick: React.MouseEventHandler<HTMLButtonElement> = (
    e
  ) => {
    if (!(e.target instanceof HTMLButtonElement)) return;
    setJobTypeFilter(e.target.value);
  };

  return (
    <S.Container>
      <S.SearchWrapper>
        <SearchBar
          careerLevelFilter={careerLevelFilter}
          jobTypeFilter={jobTypeFilter}
        />
        <SearchFilter
          careerLevelFilter={careerLevelFilter}
          jobTypeFilter={jobTypeFilter}
          handleCareerLevelFilterClick={handleCareerLevelFilterClick}
          handleJobTypeFilterClick={handleJobTypeFilterClick}
        />
      </S.SearchWrapper>
      <S.ProfileCardWrapper>
        <ProfileCard />
        <ProfileCard />
        <ProfileCard />
        <ProfileCard />
        <ProfileCard />
        <ProfileCard />
      </S.ProfileCardWrapper>
    </S.Container>
  );
}

export default ProfileSearch;
