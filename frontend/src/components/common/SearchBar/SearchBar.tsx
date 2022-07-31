import { useState } from 'react';

import * as S from '@/components/common/SearchBar/SearchBar.style';
import SearchImage from '@/assets/search.svg';

type Props = {
  careerLevelFilter: string;
  jobTypeFilter: string;
};

function SearchBar({ careerLevelFilter, jobTypeFilter }: Props) {
  const [searchInput, setSearchInput] = useState('');

  const handleInputChange: React.ChangeEventHandler<HTMLInputElement> = (e) => {
    if (!(e.target instanceof HTMLInputElement)) return;
    setSearchInput(e.target.value);
  };

  const handleSubmit: React.FormEventHandler<HTMLFormElement> = (e) => {
    if (!(e.target instanceof HTMLFormElement)) return;
    e.preventDefault();

    console.log(careerLevelFilter, jobTypeFilter, searchInput);
  };

  return (
    <S.Container>
      <S.Input type="text" value={searchInput} onChange={handleInputChange} />
      <S.Form onSubmit={handleSubmit}>
        <S.Button type="submit">
          <SearchImage />
        </S.Button>
      </S.Form>
    </S.Container>
  );
}

export default SearchBar;
