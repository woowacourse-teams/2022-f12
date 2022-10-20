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

  const closeOnEscape = (e: KeyboardEvent) => {
    if (e.code !== 'Escape') return;
    handleClose();
  };

  const preventScroll = (scrollY: number) => () => {
    window.scrollTo(0, scrollY);
  };

  const handleFocusRemove = () => {
    const root = document.querySelector<HTMLElement>('#root');
    root.setAttribute('inert', 'true');
  };

  const handleFocusRetrieve = () => {
    const root = document.querySelector<HTMLElement>('#root');
    root.removeAttribute('inert');
  };

  useEffect(() => {
    const scrollY = window.scrollY;
    setScrollOffset(scrollY);
    const preventScrollHandler = preventScroll(scrollY);

    handleFocusRemove();
    window.addEventListener('keydown', closeOnEscape);
    window.addEventListener('scroll', preventScrollHandler);

    return () => {
      handleFocusRetrieve();
      window.removeEventListener('keydown', closeOnEscape);
      window.removeEventListener('scroll', preventScrollHandler);
    };
  }, []);

  return createPortal(
    <S.Container
      scrollOffset={scrollOffset}
      onTransitionEnd={handleUnmount}
      role="dialog"
    >
      <S.Backdrop onClick={handleClose} animationTrigger={animationTrigger} />
      <S.Content tabIndex={0} animationTrigger={animationTrigger}>
        {children}
        <ActionButtons handleClose={handleClose} handleConfirm={handleConfirm} />
      </S.Content>
    </S.Container>,
    document.querySelector('body')
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
