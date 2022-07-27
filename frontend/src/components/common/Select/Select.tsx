import React, { useMemo } from 'react';
import * as S from '@/components/common/Select/Select.style';

type Props = {
  value: string;
  setValue: React.Dispatch<React.SetStateAction<string>>;
  options: { value: string; text: string }[];
};

function Select({ value, setValue, options }: Props) {
  const handleOptionChange: React.ChangeEventHandler<HTMLSelectElement> = ({
    target: { value },
  }) => {
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
    <S.Container value={value} onChange={handleOptionChange}>
      {optionComponents}
    </S.Container>
  );
}

export default Select;
