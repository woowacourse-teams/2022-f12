import ProductBar from '@/components/common/ProductBar/ProductBar';
import { useReducer } from 'react';
import DownArrow from '@/assets/down_arrow.svg';

import * as S from '@/components/common/ProductSelect/ProductSelect.style';
import theme from '@/style/theme';

type Props = {
  options: InventoryProduct[];
  value: InventoryProduct;
  setValue: (value: InventoryProduct) => void;
};

function ProductSelect({ options, value, setValue }: Props) {
  const [isOpen, setOpen] = useReducer((isOpen: boolean) => !isOpen, false);
  const currentProduct = options.find(
    ({ inventoryId }) => inventoryId === value.inventoryId
  );

  const handleProductSelect = (value: InventoryProduct) => {
    setValue(value);
    setOpen();
  };

  const otherOptions = options.filter(
    ({ inventoryId }) => inventoryId !== value.inventoryId
  );

  const OptionListItems = otherOptions.map((inventoryProduct) => (
    <S.Option key={inventoryProduct.id}>
      <S.PseudoButton onClick={() => handleProductSelect(inventoryProduct)}>
        <ProductBar name={inventoryProduct.name} barType="default" />
      </S.PseudoButton>
    </S.Option>
  ));

  return (
    <S.Container>
      <S.Selected>
        <S.PseudoButton onClick={setOpen}>
          <ProductBar name={currentProduct.name} barType="selected" />
        </S.PseudoButton>
        <DownArrow stroke={theme.colors.black} />
      </S.Selected>
      {isOpen && <S.OptionsList>{OptionListItems}</S.OptionsList>}
    </S.Container>
  );
}

export default ProductSelect;
