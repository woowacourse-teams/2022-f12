import ProductBar from '@/components/common/ProductBar/ProductBar';
import { useReducer, useState } from 'react';
import DownArrow from '@/assets/down_arrow.svg';

import * as S from '@/components/common/ProductSelect/ProductSelect.style';
import theme from '@/style/theme';

type Props = {
  options: InventoryProduct[];
  initialValue: InventoryProduct;
};

function ProductSelect({ options, initialValue }: Props) {
  const [isEditMode, setEditMode] = useReducer(
    (isEditMode: boolean) => !isEditMode,
    false
  );
  const [isOptionsOpen, setOptionOpen] = useReducer(
    (isOptionsOpen: boolean) => !isOptionsOpen,
    false
  );
  const [profileProduct, setProfileProduct] = useState(initialValue);

  const handleProductSelect = (value: InventoryProduct) => {
    setProfileProduct(value);
    setOptionOpen();
  };

  const otherOptions = options.filter(
    ({ inventoryId }) => inventoryId !== profileProduct.inventoryId
  );

  const OptionListItems = otherOptions.map((inventoryProduct) => (
    <S.Option key={inventoryProduct.id}>
      <S.PseudoButton onClick={() => handleProductSelect(inventoryProduct)}>
        <ProductBar name={inventoryProduct.name} barType="default" />
      </S.PseudoButton>
    </S.Option>
  ));

  const handleEditDone = () => {
    if (isEditMode) {
      // handleProfileProductPatch()
    }
    setEditMode();
  };

  return (
    <S.Container>
      <S.EditButton onClick={handleEditDone}>
        {isEditMode ? '수정 완료' : '수정하기'}
      </S.EditButton>
      {isEditMode ? (
        <>
          <S.Selected>
            <S.PseudoButton onClick={setOptionOpen}>
              <ProductBar name={profileProduct.name} barType="selected" />
            </S.PseudoButton>
            <DownArrow stroke={theme.colors.black} />
          </S.Selected>
          {isOptionsOpen && <S.OptionsList>{OptionListItems}</S.OptionsList>}
        </>
      ) : (
        <ProductBar name={profileProduct.name} barType={'selected'} />
      )}
    </S.Container>
  );
}

export default ProductSelect;
