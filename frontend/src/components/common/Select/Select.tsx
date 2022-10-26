import React, { useMemo } from 'react';

import * as S from '@/components/common/Select/Select.style';

type Props<T extends string> = {
  value: T;
  setValue: React.Dispatch<React.SetStateAction<T>>;
  options: { value: T; text: string }[];
};

const isValidValue = <T,>(
  input: unknown,
  options: { value: T; text: string }[]
): input is T => options.some(({ value }) => value === input);

function Select<T extends string>({ value, setValue, options }: Props<T>) {
  const handleOptionChange: React.ChangeEventHandler<HTMLSelectElement> = ({
    target: { value },
  }) => {
    if (!isValidValue<T>(value, options)) return;
    setValue(value);
  };

  const optionComponents = useMemo(
    () =>
      options.map(({ value, text }) => (
        <S.Option key={value} value={value}>
          {text}
        </S.Option>
      )),
    [options]
  );

  return (
    <S.Container value={value} onChange={handleOptionChange} aria-label={'정렬 기준'}>
      {optionComponents}
    </S.Container>
  );
}

export default Select;
