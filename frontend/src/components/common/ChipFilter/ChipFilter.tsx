import * as S from '@/components/common/ChipFilter/ChipFilter.style';

type Props = {
  fontSize: number;
  value: string;
  children: React.ReactNode;
  careerLevelFilter?: string;
  jobTypeFilter?: string;
  handleClick: (e) => void;
};

function ChipFilter({
  fontSize,
  value,
  children,
  careerLevelFilter,
  jobTypeFilter,
  handleClick,
}: Props) {
  return (
    <S.Button
      fontSize={fontSize}
      clicked={careerLevelFilter === value || jobTypeFilter === value}
      value={value}
      onClick={handleClick}
    >
      {children}
    </S.Button>
  );
}

export default ChipFilter;
