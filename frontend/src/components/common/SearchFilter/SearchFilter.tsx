import { useMemo } from 'react';

import ChipFilter from '@/components/common/ChipFilter/ChipFilter';
import * as S from '@/components/common/SearchFilter/SearchFilter.style';

type Entries<T> = {
  [K in keyof T]: [K, T[K]];
}[keyof T][];

type Props<T extends string> = {
  title: string;
  value: T;
  setValue: React.Dispatch<React.SetStateAction<T>>;
  options: Record<T, string>;
};

const isValidValue = <T extends string>(
  input: string,
  options: Record<T, string>
): input is T => input in options;

function SearchFilter<T extends string>({ title, value, setValue, options }: Props<T>) {
  const handleFilterClick: React.MouseEventHandler<HTMLButtonElement> = ({ target }) => {
    if (!(target instanceof HTMLButtonElement)) return;
    const inputValue = target.value;
    if (inputValue === value) {
      setValue(null);
      return;
    }

    if (!isValidValue(inputValue, options)) return;
    setValue(inputValue);
  };

  const optionEntries = useMemo(() => Object.entries(options), []) as Entries<
    typeof options
  >;

  return (
    <S.Container>
      <S.Wrapper role={'radiogroup'} aria-label={'카테고리 선택하기'}>
        <S.FilterTitle aria-hidden={true}>{title}</S.FilterTitle>
        {optionEntries.map(([key, content], index: number) => {
          return (
            <ChipFilter
              key={index}
              fontSize={13}
              value={key}
              filter={value}
              handleClick={handleFilterClick}
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
