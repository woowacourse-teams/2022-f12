import { useState } from 'react';

import ProductBar from '@/components/common/ProductBar/ProductBar';
import * as S from '@/components/common/ProductSelect/ProductSelect.style';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';

type Props = {
  submitHandler?: () => void;
  updateProfileProduct?: (array: number[]) => Promise<boolean>;
  inventoryList: Record<string, InventoryProduct[]>;
  editable: boolean;
};

const categories = {
  keyboard: '키보드',
  mouse: '마우스',
  monitor: '모니터',
  stand: '거치대',
  software: '소프트웨어',
} as const;

function ProductSelect({
  submitHandler,
  updateProfileProduct,
  inventoryList,
  editable,
}: Props) {
  const selectedItems = Object.values(inventoryList)
    .flat()
    .filter(({ selected }) => selected);

  const selectedItemsIdsByCategory = Object.values(selectedItems).reduce(
    (ids, { id, product: { category } }) => {
      return { ...ids, [category]: id };
    },
    {}
  );

  const [isEditMode, setEditMode] = useState(false);
  const [selectedState, setSelectedState] = useState(selectedItemsIdsByCategory);

  const handleEditDone = async () => {
    if (!editable) return;
    if (isEditMode) {
      const didPatch = await updateProfileProduct(Object.values(selectedState));
      if (didPatch) submitHandler();
      setEditMode(false);

      return;
    }

    setEditMode(true);
  };

  const handleSelect = (key: string, id: number) => {
    if (Object.values(selectedState).includes(id)) {
      const newState = { ...selectedState };
      delete newState[key];
      setSelectedState(newState);
      return;
    }

    setSelectedState((prev) => {
      return { ...prev, [key]: id };
    });
  };

  return (
    <S.Container>
      {editable && (
        <S.EditButton onClick={handleEditDone}>
          {isEditMode ? '수정 완료' : '수정하기'}
        </S.EditButton>
      )}
      <SectionHeader title="대표 장비" />
      {isEditMode ? (
        <S.OptionsContainer>
          {Object.entries(inventoryList)
            .filter(([category]) => category !== 'software')
            .map(([category, items]) => (
              <S.OptionsList key={category}>
                <p>{categories[category]}</p>
                {items.map((inventoryProduct) => {
                  return (
                    <S.Option key={inventoryProduct.id}>
                      <S.PseudoButton
                        onClick={() => handleSelect(category, inventoryProduct.id)}
                      >
                        <ProductBar
                          name={inventoryProduct.product.name}
                          barType={
                            selectedState[category] === inventoryProduct.id
                              ? 'selected'
                              : 'default'
                          }
                        />
                      </S.PseudoButton>
                    </S.Option>
                  );
                })}
              </S.OptionsList>
            ))}
        </S.OptionsContainer>
      ) : selectedItems.length !== 0 ? (
        selectedItems.map(({ id, product: { name } }) => (
          <ProductBar key={id} name={name} barType={'selected'} />
        ))
      ) : (
        <S.NoContentMessage>
          등록된 장비가 없어요! {editable && '수정하기로 대표 장비를 등록해주세요!'}
        </S.NoContentMessage>
      )}
    </S.Container>
  );
}

export default ProductSelect;
