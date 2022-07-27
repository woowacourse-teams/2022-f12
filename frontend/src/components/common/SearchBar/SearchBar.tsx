import * as S from '@/components/common/SearchBar/SearchBar.style';
import SearchImage from '@/assets/search.svg';

function SearchBar() {
  return (
    <S.Container>
      <S.Input type="text" />
      <S.Button type="submit">
        <SearchImage />
      </S.Button>
    </S.Container>
  );
}

export default SearchBar;
