import { PropsWithChildren } from 'react';

import * as S from '@/components/common/Chip/Chip.style';

type Props = {
  size: 's' | 'l';
};

function Chip({ size, children }: PropsWithChildren<Props>) {
  return <S.Container size={size}>{children}</S.Container>;
}

export default Chip;
