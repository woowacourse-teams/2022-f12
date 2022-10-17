import { PropsWithChildren } from 'react';
import { createPortal } from 'react-dom';

import * as S from '@/components/common/BottomSheet/BottomSheet.style';

type Props = {
  container?: Element | DocumentFragment;
  handleClose: () => void;
  handleUnmount?: () => void;
  animationTrigger?: boolean;
};

function BottomSheet({
  children,
  handleClose,
  handleUnmount,
  animationTrigger,
}: PropsWithChildren<Props>) {
  return createPortal(
    <S.Container
      role="dialog"
      onTransitionEnd={handleUnmount}
      animationTrigger={animationTrigger}
    >
      <S.Backdrop onClick={handleClose} />
      <S.Content>{children}</S.Content>
    </S.Container>,
    document.querySelector('#root')
  );
}

export default BottomSheet;
