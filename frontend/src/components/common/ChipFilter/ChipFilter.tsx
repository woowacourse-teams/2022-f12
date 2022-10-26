import { MouseEventHandler, PropsWithChildren } from 'react';

import * as S from '@/components/common/ChipFilter/ChipFilter.style';

type Props = {
  fontSize: number;
  value: string;
  filter?: string;
  handleClick: MouseEventHandler;
};

function ChipFilter({
  fontSize,
  value,
  children,
  filter,
  handleClick,
}: PropsWithChildren<Props>) {
  const isChecked = filter === value;
  return (
    <S.Button
      fontSize={fontSize}
      clicked={isChecked}
      value={value}
      onClick={handleClick}
      role="radio"
      aria-checked={filter === value}
    >
      {children}
    </S.Button>
  );
}

export default ChipFilter;
