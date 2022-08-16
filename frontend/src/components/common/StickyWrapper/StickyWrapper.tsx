import { PropsWithChildren } from 'react';

import * as S from '@/components/common/StickyWrapper/StickyWrapper.style';

function StickyWrapper({ children }: PropsWithChildren) {
  return <S.Container>{children}</S.Container>;
}

export default StickyWrapper;
