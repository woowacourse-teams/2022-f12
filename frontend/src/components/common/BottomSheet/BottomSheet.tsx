import { PropsWithChildren } from 'react';
import * as S from '@/components/common/BottomSheet/BottomSheet.style';
import { createPortal } from 'react-dom';

type Props = {
  container?: Element | DocumentFragment;
  handleClose: () => void;
};

function BottomSheet({ children, handleClose }: PropsWithChildren<Props>) {
  return createPortal(
    <S.Container>
      <S.Backdrop onClick={handleClose} />
      <S.Content>{children}</S.Content>
    </S.Container>,
    document.querySelector('#root')
  );
}

export default BottomSheet;
