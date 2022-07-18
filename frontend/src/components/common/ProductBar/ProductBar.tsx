import * as S from '@/components/common/ProductBar/ProductBar.style';
import Plus from '@/assets/plus.svg';
import theme from '@/style/theme';

type BarType = 'default' | 'selected' | 'add';

type Props = {
  name: string;
  barType: BarType;
  handleClick?: React.MouseEventHandler<HTMLDivElement>;
};

function ProductBar({ name, barType, handleClick }: Props) {
  return (
    <S.Container barType={barType} onClick={handleClick}>
      {name}
    </S.Container>
  );
}

function AddButton({ handleClick }: Pick<Props, 'handleClick'>) {
  return (
    <S.AddContainer barType={'add'} onClick={handleClick}>
      <Plus stroke={theme.colors.gray} />
    </S.AddContainer>
  );
}

ProductBar.AddButton = AddButton;

export default ProductBar;
