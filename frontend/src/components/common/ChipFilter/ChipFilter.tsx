import * as S from '@/components/common/ChipFilter/ChipFilter.style';

type Props = {
  fontSize: number;
  value: string;
  children: React.ReactNode;
  filter?: string;
  handleClick: (e) => void;
};

function ChipFilter({ fontSize, value, children, filter, handleClick }: Props) {
  return (
    <S.Button
      fontSize={fontSize}
      clicked={filter === value}
      value={value}
      onClick={handleClick}
    >
      {children}
    </S.Button>
  );
}

export default ChipFilter;
