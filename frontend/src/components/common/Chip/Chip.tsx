import * as S from '@/components/common/Chip/Chip.style';

type Props = {
  size: 's' | 'l';
  children: React.ReactNode;
};

function Chip({ size, children }: Props) {
  return <S.Container size={size}>{children}</S.Container>;
}

export default Chip;
