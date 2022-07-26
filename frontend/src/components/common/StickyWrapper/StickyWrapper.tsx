import * as S from '@/components/common/StickyWrapper/StickyWrapper.style';
import { PropsWithChildren } from 'react';

function StickyWrapper({ children }: PropsWithChildren) {
  return <S.Container>{children}</S.Container>;
}

export default StickyWrapper;
