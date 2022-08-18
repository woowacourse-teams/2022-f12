import { Fragment, useState } from 'react';

import SectionHeader from '@/components/common/SectionHeader/SectionHeader';

import DeskSetupCard from '@/components/DeskSetupCard/DeskSetupCard';
import * as S from '@/components/Profile/InventoryProductList/InventoryProductList.style';

import { CATEGORIES } from '@/constants/product';

type Props = {
  submitHandler?: () => void;
  updateProfileProduct?: (array: number[]) => Promise<boolean>;
  inventoryList: Record<string, InventoryProduct[]>;
  editable: boolean;
};

function InventoryProductList({
  inventoryList,
  editable,
  submitHandler,
  updateProfileProduct,
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

  const handleEdit = async () => {
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
    <>
      <S.FlexWrapper>
        <SectionHeader title={'리뷰를 작성한 제품'} />
        {editable && (
          <S.EditDeskSetupButton onClick={handleEdit}>
            {isEditMode ? '데스크 셋업 변경 완료' : '데스크 셋업 변경하기'}
          </S.EditDeskSetupButton>
        )}
      </S.FlexWrapper>
      {isEditMode ? (
        Object.entries(inventoryList)
          .filter(([category]) => category !== 'software')
          .map(([category, items]) => (
            <Fragment key={category}>
              <S.CategoryTitle>{CATEGORIES[category]}</S.CategoryTitle>
              <S.Container>
                {items.map((item) => (
                  <S.PseudoButton
                    key={item?.product.id}
                    onClick={() => {
                      handleSelect(category, item.id);
                    }}
                  >
                    <DeskSetupCard
                      key={item?.product.id}
                      item={item}
                      borderType={
                        selectedState[category] === item.id
                          ? 'selectedAnimation'
                          : 'default'
                      }
                      size={'s'}
                      isEditMode={isEditMode}
                    />
                  </S.PseudoButton>
                ))}
              </S.Container>
            </Fragment>
          ))
      ) : Object.keys(inventoryList).length > 0 ? (
        Object.entries(inventoryList).map(([category, items]) => (
          <Fragment key={category}>
            <S.CategoryTitle>{CATEGORIES[category]}</S.CategoryTitle>
            <S.Container>
              {items.map((item) => (
                <DeskSetupCard
                  key={item?.product.id}
                  item={item}
                  borderType={
                    selectedState[category] === item.id ? 'selected' : 'default'
                  }
                  size={'s'}
                />
              ))}
            </S.Container>
          </Fragment>
        ))
      ) : (
        <S.NoContents>리뷰를 작성한 제품이 없어요</S.NoContents>
      )}
    </>
  );
}

export default InventoryProductList;
