import * as S from '@/components/common/SearchBar/SearchBar.style';
import SearchImage from '@/assets/search.svg';

type Props = {
  searchInput: string;
  setSearchInput: (string) => void;
};

function SearchBar({ searchInput, setSearchInput }: Props) {
  const handleInputChange: React.ChangeEventHandler<HTMLInputElement> = (e) => {
    if (!(e.target instanceof HTMLInputElement)) return;
    setSearchInput(e.target.value);
  };

  return (
    <S.Container>
      <S.Input type="text" value={searchInput} onChange={handleInputChange} />
      <S.Button>
        <SearchImage />
      </S.Button>
    </S.Container>
  );
}

export default SearchBar;
