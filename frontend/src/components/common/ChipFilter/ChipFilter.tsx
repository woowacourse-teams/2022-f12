import * as S from '@/components/common/ChipFilter/ChipFilter.style';

type Props = {
  fontSize: number;
  clicked: boolean;
  children: React.ReactNode;
};

function ChipFilter({ fontSize, clicked, children }: Props) {
  return (
    <S.Button fontSize={fontSize} clicked={clicked}>
      {children}
    </S.Button>
  );
}

export default ChipFilter;
