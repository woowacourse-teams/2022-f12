import ProductBar from '@/components/common/ProductBar/ProductBar';
import { useReducer } from 'react';
import DownArrow from '@/assets/down_arrow.svg';

import * as S from '@/components/common/ProductSelect/ProductSelect.style';
import theme from '@/style/theme';
import useInventory from '@/hooks/useInventory';

type Props = {
  submitHandler: () => void;
};

function ProductSelect({ submitHandler }: Props) {
  const [isEditMode, setEditMode] = useReducer(
    (isEditMode: boolean) => !isEditMode,
    false
  );
  const [isOptionsOpen, setOptionOpen] = useReducer(
    (isOptionsOpen: boolean) => !isOptionsOpen,
    false
  );
  const {
    selectedProduct,
    setSelectedProduct,
    otherProducts,
    updateProfileProduct,
  } = useInventory();

  const handleProductSelect = (value: InventoryProduct) => {
    setSelectedProduct(value);
    setOptionOpen();
  };

  const handleEditDone = () => {
    if (isEditMode) {
      updateProfileProduct(selectedProduct.id)
        .then(() => {
          submitHandler();
          setOptionOpen();
          setEditMode();
        })
        .catch((error) => {
          console.log(error);
        });
      return;
    }

    setEditMode();
  };

  return (
    otherProducts &&
    otherProducts.length !== 0 &&
    selectedProduct && (
      <S.Container>
        <S.EditButton onClick={handleEditDone}>
          {isEditMode ? '수정 완료' : '수정하기'}
        </S.EditButton>
        {isEditMode ? (
          <>
            <S.Selected>
              <S.PseudoButton onClick={setOptionOpen}>
                <ProductBar
                  name={selectedProduct.product.name}
                  barType="selected"
                />
              </S.PseudoButton>
              <DownArrow stroke={theme.colors.black} />
            </S.Selected>
            {isOptionsOpen && (
              <S.OptionsList>
                <OptionListItems
                  options={otherProducts}
                  handleSelect={handleProductSelect}
                />
              </S.OptionsList>
            )}
          </>
        ) : (
          <ProductBar
            name={selectedProduct.product.name}
            barType={'selected'}
          />
        )}
      </S.Container>
    )
  );
}

type OptionProps = {
  options: InventoryProduct[];
  handleSelect: (inventoryProduct: InventoryProduct) => void;
};

function OptionListItems({ options, handleSelect }: OptionProps) {
  return (
    <>
      {options.map((inventoryProduct) => {
        return (
          <S.Option key={inventoryProduct.id}>
            <S.PseudoButton onClick={() => handleSelect(inventoryProduct)}>
              <ProductBar
                name={inventoryProduct.product.name}
                barType="default"
              />
            </S.PseudoButton>
          </S.Option>
        );
      })}
    </>
  );
}

export default ProductSelect;
