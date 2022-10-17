import { PropsWithChildren } from 'react';

import * as S from '@/components/common/FloatingButton/FloatingButton.style';

type Props = {
  label: string;
  clickHandler: () => void;
};

function FloatingButton({ clickHandler, label, children }: PropsWithChildren<Props>) {
  return (
    <S.Container>
      <S.Button aria-label={label} onClick={clickHandler}>
        {children}
      </S.Button>
    </S.Container>
  );
}

export default FloatingButton;
