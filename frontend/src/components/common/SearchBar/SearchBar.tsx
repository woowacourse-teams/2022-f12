import * as S from '@/components/common/SearchBar/SearchBar.style';

import SearchImage from '@/assets/search.svg';

type Props = {
  searchInput: string;
  setSearchInput: (string) => void;
};

function SearchBar({ searchInput, setSearchInput }: Props) {
  const handleInputChange: React.ChangeEventHandler<HTMLInputElement> = ({ target }) => {
    if (!(target instanceof HTMLInputElement)) return;
    const newValue = target.value === '' ? null : target.value;
    setSearchInput(newValue);
  };

  return (
    <S.Container>
      <S.Input
        placeholder={'검색어를 입력해주세요'}
        maxLength={100}
        type="text"
        value={searchInput || ''}
        onChange={handleInputChange}
      />
      <S.Button aria-hidden={true}>
        <SearchImage />
      </S.Button>
    </S.Container>
  );
}

export default SearchBar;
