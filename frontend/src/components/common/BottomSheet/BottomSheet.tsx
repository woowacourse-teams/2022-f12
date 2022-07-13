import { PropsWithChildren } from 'react';
import * as S from '@/components/common/BottomSheet/BottomSheet.style';

type Props = {
  container?: Element | DocumentFragment;
  closeHandler: () => void;
};

function BottomSheet({ children, closeHandler }: PropsWithChildren<Props>) {
  return (
    <S.Container>
      <S.Backdrop onClick={closeHandler} />
      <S.Content>{children}</S.Content>
    </S.Container>
  );
}

export default BottomSheet;
