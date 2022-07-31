import { createPortal } from 'react-dom';
import * as S from '@/components/common/Modal/Modal.style';
import { PropsWithChildren } from 'react';

type Props = {
  handleClose: () => void;
  handleConfirm?: () => void;
};

function Modal({
  handleClose,
  handleConfirm,
  children,
}: PropsWithChildren<Props>) {
  return createPortal(
    <S.Container>
      <S.Backdrop onClick={handleClose} />
      <S.Content>
        {children}
        <ActionButtons
          handleClose={handleClose}
          handleConfirm={handleConfirm}
        />
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
      {handleConfirm && (
        <S.ConfirmButton onClick={handleConfirm}>확인</S.ConfirmButton>
      )}
    </S.ButtonContainer>
  );
}

Modal.Title = Title;
Modal.Body = Body;

export default Modal;
