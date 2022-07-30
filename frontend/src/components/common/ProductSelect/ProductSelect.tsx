import ProductBar from '@/components/common/ProductBar/ProductBar';
import { useReducer } from 'react';
import DownArrow from '@/assets/down_arrow.svg';

import * as S from '@/components/common/ProductSelect/ProductSelect.style';
import theme from '@/style/theme';

type Props = {
  submitHandler: () => void;
  selectedProduct: InventoryProduct;
  setSelectedProduct: React.Dispatch<React.SetStateAction<InventoryProduct>>;
  otherProducts: InventoryProduct[];
  updateProfileProduct: () => Promise<void>;
};

function ProductSelect({
  submitHandler,
  selectedProduct,
  setSelectedProduct,
  otherProducts,
  updateProfileProduct,
}: Props) {
  const [isEditMode, setEditMode] = useReducer(
    (isEditMode: boolean) => !isEditMode,
    false
  );
  const [isOptionsOpen, setOptionOpen] = useReducer(
    (isOptionsOpen: boolean) => !isOptionsOpen,
    false
  );

  const handleProductSelect = (value: InventoryProduct) => {
    setSelectedProduct(value);
    setOptionOpen();
  };

  const handleEditDone = () => {
    if (isEditMode) {
      updateProfileProduct()
        .then(() => {
          submitHandler();
          setOptionOpen();
          setEditMode();
        })
        .catch((error) => {
          console.error(error);
        });
      return;
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
              {selectedProduct !== undefined ? (
                <ProductBar
                  name={selectedProduct.product.name}
                  barType="selected"
                />
              ) : (
                <ProductBar.AddButton />
              )}
              <DownArrow stroke={theme.colors.black} />
            </S.PseudoButton>
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
      ) : selectedProduct ? (
        <ProductBar name={selectedProduct.product.name} barType={'selected'} />
      ) : (
        <S.NoContentMessage>
          등록된 장비가 없어요! 수정하기로 대표 장비를 등록해주세요!
        </S.NoContentMessage>
      )}
    </S.Container>
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
