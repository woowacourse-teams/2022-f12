import { PropsWithChildren } from 'react';
import * as S from '@/components/common/Chip/Chip.style';

function Chip({ children }: PropsWithChildren) {
  return <S.Container>{children}</S.Container>;
}

export default Chip;
