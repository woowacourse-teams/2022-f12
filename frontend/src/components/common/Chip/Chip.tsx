import * as S from '@/components/common/Chip/Chip.style';

type Props = {
  paddingTopBottom: number;
  paddingLeftRight: number;
  fontSize: number;
  children: React.ReactNode;
};

function Chip({
  paddingTopBottom,
  paddingLeftRight,
  fontSize,
  children,
}: Props) {
  return (
    <S.Container
      paddingTopBottom={paddingTopBottom}
      paddingLeftRight={paddingLeftRight}
      fontSize={fontSize}
    >
      {children}
    </S.Container>
  );
}

export default Chip;
