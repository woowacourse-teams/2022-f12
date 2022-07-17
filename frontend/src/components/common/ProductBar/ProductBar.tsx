import * as S from '@/components/common/ProductBar/ProductBar.style';

type Props = {
  name: string;
  isSelected: boolean;
  handleClick?: React.MouseEventHandler<HTMLDivElement>;
};

function ProductBar({ name, isSelected, handleClick }: Props) {
  return (
    <S.Container isSelected={isSelected} onClick={handleClick}>
      {name}
    </S.Container>
  );
}

export default ProductBar;
