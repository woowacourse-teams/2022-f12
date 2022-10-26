import { PropsWithChildren, useEffect } from 'react';
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
  const closeOnEscape = (e: KeyboardEvent) => {
    if (e.code !== 'Escape') return;
    handleClose();
  };

  useEffect(() => {
    window.addEventListener('keydown', closeOnEscape);
    return () => {
      window.removeEventListener('keydown', closeOnEscape);
    };
  }, []);
  return createPortal(
    <S.Container
      role="dialog"
      onTransitionEnd={handleUnmount}
      animationTrigger={animationTrigger}
    >
      <S.Backdrop onClick={handleClose} />
      <S.Content>{children}</S.Content>
    </S.Container>,
    document.querySelector('#root') || document.body
  );
}

export default BottomSheet;
