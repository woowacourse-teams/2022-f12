import ChipFilter from '@/components/common/ChipFilter/ChipFilter';
import * as S from '@/components/SearchFilter/SearchFilter.style';

function SearchFilter() {
  return (
    <S.Container>
      <S.Wrapper>
        <S.FilterTitle>경력</S.FilterTitle>
        <ChipFilter fontSize={10} clicked>
          경력 없음
        </ChipFilter>
        <ChipFilter fontSize={10} clicked={false}>
          0-3년차
        </ChipFilter>
        <ChipFilter fontSize={10} clicked={false}>
          3-5년차
        </ChipFilter>
        <ChipFilter fontSize={10} clicked={false}>
          5년차 이상
        </ChipFilter>
      </S.Wrapper>
      <S.Wrapper>
        <S.FilterTitle>직무</S.FilterTitle>
        <ChipFilter fontSize={10} clicked>
          경력 없음
        </ChipFilter>
        <ChipFilter fontSize={10} clicked={false}>
          0-3년차
        </ChipFilter>
        <ChipFilter fontSize={10} clicked={false}>
          3-5년차
        </ChipFilter>
        <ChipFilter fontSize={10} clicked={false}>
          5년차 이상
        </ChipFilter>
      </S.Wrapper>
    </S.Container>
  );
}

export default SearchFilter;
