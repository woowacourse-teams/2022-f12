import { PropsWithChildren } from 'react';

import * as S from '@/components/common/SectionHeader/SectionHeader.style';

type Props = {
  title: string;
};

function SectionHeader({ title, children }: PropsWithChildren<Props>) {
  return (
    <S.Container>
      <S.Title>{title}</S.Title>
      {children}
    </S.Container>
  );
}

export default SectionHeader;
