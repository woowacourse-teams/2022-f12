import ChipFilter from '@/components/common/ChipFilter/ChipFilter';
import * as S from '@/components/SearchFilter/SearchFilter.style';

type Props = {
  title: string;
  value: string;
  handleValueClick: (e) => void;
  options: Record<string, string>;
};

function SearchFilter({ title, value, handleValueClick, options }: Props) {
  return (
    <S.Container>
      <S.Wrapper>
        <S.FilterTitle>{title}</S.FilterTitle>
        {Object.entries(options).map(([key, content], index: number) => {
          return (
            <ChipFilter
              key={index}
              fontSize={14}
              value={key}
              filter={value}
              handleClick={handleValueClick}
            >
              {content}
            </ChipFilter>
          );
        })}
      </S.Wrapper>
    </S.Container>
  );
}

export default SearchFilter;
