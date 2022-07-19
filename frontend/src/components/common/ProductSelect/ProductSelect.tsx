import ProductBar from '@/components/common/ProductBar/ProductBar';
import { useReducer, useState } from 'react';
import DownArrow from '@/assets/down_arrow.svg';

import * as S from '@/components/common/ProductSelect/ProductSelect.style';
import theme from '@/style/theme';
import useInventory from '@/hooks/useInventory';

type Props = {
  options: InventoryProduct[];
  initialValue: InventoryProduct;
  submitHandler: () => void;
};

function ProductSelect({ options, initialValue, submitHandler }: Props) {
  const [isEditMode, setEditMode] = useReducer(
    (isEditMode: boolean) => !isEditMode,
    false
  );
  const [isOptionsOpen, setOptionOpen] = useReducer(
    (isOptionsOpen: boolean) => !isOptionsOpen,
    false
  );
  const [profileProduct, setProfileProduct] = useState(initialValue);
  const { updateProfileProduct } = useInventory();

  const handleProductSelect = (value: InventoryProduct) => {
    setProfileProduct(value);
    setOptionOpen();
  };

  const otherOptions = options.filter(({ id }) => id !== profileProduct.id);

  const OptionListItems = otherOptions.map((inventoryProduct) => {
    const {
      id,
      product: { name },
    } = inventoryProduct;

    return (
      <S.Option key={id}>
        <S.PseudoButton onClick={() => handleProductSelect(inventoryProduct)}>
          <ProductBar name={name} barType="default" />
        </S.PseudoButton>
      </S.Option>
    );
  });

  const handleEditDone = () => {
    if (isEditMode) {
      updateProfileProduct(profileProduct.id, initialValue.id)
        .then(() => {
          submitHandler();
        })
        .catch((error) => {
          alert(error);
        });
      setOptionOpen();
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
              <ProductBar
                name={profileProduct.product.name}
                barType="selected"
              />
            </S.PseudoButton>
            <DownArrow stroke={theme.colors.black} />
          </S.Selected>
          {isOptionsOpen && <S.OptionsList>{OptionListItems}</S.OptionsList>}
        </>
      ) : (
        <ProductBar name={profileProduct.product.name} barType={'selected'} />
      )}
    </S.Container>
  );
}

export default ProductSelect;
