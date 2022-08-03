import ProductBar from '@/components/common/ProductBar/ProductBar';
import { useState } from 'react';

import * as S from '@/components/common/ProductSelect/ProductSelect.style';

type Props = {
  submitHandler: () => void;
  updateProfileProduct: (array: number[]) => Promise<boolean>;
  inventoryList: Record<string, InventoryProduct[]>;
};

function ProductSelect({
  submitHandler,
  updateProfileProduct,
  inventoryList,
}: Props) {
  const initSelected = Object.entries(inventoryList).reduce(
    (obj, [key, products]) => {
      const selectedItem = products.find(({ selected }) => selected);
      if (!selectedItem) {
        return { ...obj };
      }
      return { ...obj, [key]: selectedItem.id };
    },
    {}
  );
  const [isEditMode, setEditMode] = useState(false);
  const [selectedState, setSelectedState] = useState(initSelected);

  const selectedItems = Object.values(inventoryList)
    .flat()
    .filter(({ selected }) => selected);

  const handleEditDone = () => {
    if (isEditMode) {
      updateProfileProduct(Object.values(selectedState))
        .then((didPatch) => {
          if (didPatch) submitHandler();
          setEditMode(false);
        })
        .catch((error) => {
          console.error(error);
        });
      return;
    }

    setEditMode(true);
  };

  const handleSelect = (key: string, id: number) => {
    setSelectedState((prev) => {
      return { ...prev, [key]: id };
    });
  };

  return (
    <S.Container>
      <S.EditButton onClick={handleEditDone}>
        {isEditMode ? '수정 완료' : '수정하기'}
      </S.EditButton>
      {isEditMode ? (
        <S.OptionsContainer>
          <S.OptionsList>
            <p>키보드</p>
            <OptionListItems
              productType="keyboardItems"
              options={inventoryList.keyboardItems}
              handleSelect={handleSelect}
              selectedState={selectedState}
            />
          </S.OptionsList>
          <S.OptionsList>
            <p>마우스</p>
            <OptionListItems
              productType="mouseItems"
              options={inventoryList.mouseItems}
              handleSelect={handleSelect}
              selectedState={selectedState}
            />
          </S.OptionsList>
          <S.OptionsList>
            <p>모니터</p>
            <OptionListItems
              productType="monitorItems"
              options={inventoryList.monitorItems}
              handleSelect={handleSelect}
              selectedState={selectedState}
            />
          </S.OptionsList>
          <S.OptionsList>
            <p>스탠드</p>
            <OptionListItems
              productType="standItems"
              options={inventoryList.standItems}
              handleSelect={handleSelect}
              selectedState={selectedState}
            />
          </S.OptionsList>
        </S.OptionsContainer>
      ) : selectedItems.length !== 0 ? (
        selectedItems.map(({ id, product: { name } }) => (
          <ProductBar key={id} name={name} barType={'selected'} />
        ))
      ) : (
        <S.NoContentMessage>
          등록된 장비가 없어요! 수정하기로 대표 장비를 등록해주세요!
        </S.NoContentMessage>
      )}
    </S.Container>
  );
}

type OptionProps = {
  productType: string;
  options: InventoryProduct[];
  handleSelect: (key: string, id: number) => void;
  selectedState: object;
};

function OptionListItems({
  productType,
  options,
  handleSelect,
  selectedState,
}: OptionProps) {
  return (
    <>
      {options.map((inventoryProduct) => {
        return (
          <S.Option key={inventoryProduct.id}>
            <S.PseudoButton
              onClick={() => handleSelect(productType, inventoryProduct.id)}
            >
              <ProductBar
                name={inventoryProduct.product.name}
                barType={
                  selectedState[productType] === inventoryProduct.id
                    ? 'selected'
                    : 'default'
                }
              />
            </S.PseudoButton>
          </S.Option>
        );
      })}
    </>
  );
}

export default ProductSelect;
