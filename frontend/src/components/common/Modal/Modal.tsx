import { PropsWithChildren, useEffect, useState } from 'react';
import { createPortal } from 'react-dom';

import * as S from '@/components/common/Modal/Modal.style';

type Props = {
  handleClose: () => void;
  handleConfirm?: () => void;
  handleUnmount?: () => void;
  animationTrigger?: boolean;
};

function Modal({
  handleClose,
  handleConfirm,
  handleUnmount,
  animationTrigger = true,
  children,
}: PropsWithChildren<Props>) {
  const [scrollOffset, setScrollOffset] = useState(0);

  useEffect(() => {
    document.body.style.overflow = 'hidden';
    setScrollOffset(window.scrollY);

    return () => {
      document.body.style.overflow = 'auto';
    };
  }, []);

  return createPortal(
    <S.Container scrollOffset={scrollOffset} onTransitionEnd={handleUnmount}>
      <S.Backdrop onClick={handleClose} animationTrigger={animationTrigger} />
      <S.Content animationTrigger={animationTrigger}>
        {children}
        <ActionButtons handleClose={handleClose} handleConfirm={handleConfirm} />
      </S.Content>
    </S.Container>,
    document.querySelector('#root')
  );
}

function Title({ children }: PropsWithChildren) {
  return <S.Title>{children}</S.Title>;
}

function Body({ children }: PropsWithChildren) {
  return <S.Body>{children}</S.Body>;
}

type ActionButtonProps = {
  handleClose: () => void;
  handleConfirm?: () => void;
};

function ActionButtons({ handleClose, handleConfirm }: ActionButtonProps) {
  return (
    <S.ButtonContainer>
      <S.CloseButton onClick={handleClose}>닫기</S.CloseButton>
      {handleConfirm && <S.ConfirmButton onClick={handleConfirm}>확인</S.ConfirmButton>}
    </S.ButtonContainer>
  );
}

Modal.Title = Title;
Modal.Body = Body;

export default Modal;
