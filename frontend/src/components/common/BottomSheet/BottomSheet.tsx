import { PropsWithChildren } from 'react';
import * as S from '@/components/common/BottomSheet/BottomSheet.style';

type Props = {
  container?: Element | DocumentFragment;
  handleClose: () => void;
};

function BottomSheet({ children, handleClose }: PropsWithChildren<Props>) {
  return (
    <S.Container>
      <S.Backdrop onClick={handleClose} />
      <S.Content>{children}</S.Content>
    </S.Container>
  );
}

export default BottomSheet;
