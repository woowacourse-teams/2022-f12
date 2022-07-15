import * as S from '@/components/common/FloatingButton/FloatingButton.style';
import { PropsWithChildren } from 'react';

type Props = {
  clickHandler: () => void;
};

function FloatingButton({ clickHandler, children }: PropsWithChildren<Props>) {
  return <S.Button onClick={clickHandler}>{children}</S.Button>;
}

export default FloatingButton;
