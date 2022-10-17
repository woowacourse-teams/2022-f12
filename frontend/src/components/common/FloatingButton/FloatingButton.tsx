import { PropsWithChildren } from 'react';

import * as S from '@/components/common/FloatingButton/FloatingButton.style';

type Props = {
  clickHandler: () => void;
};

function FloatingButton({ clickHandler, children }: PropsWithChildren<Props>) {
  return (
    <S.Container>
      <S.Button aria-label="리뷰 작성하기" onClick={clickHandler}>
        {children}
      </S.Button>
    </S.Container>
  );
}

export default FloatingButton;
