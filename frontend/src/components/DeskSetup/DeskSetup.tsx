import DeskSetupCard from '@/components//DeskSetupCard/DeskSetupCard';
import * as S from '@/components/DeskSetup/DeskSetup.style';

type Props = {
  inventoryList: Record<string, InventoryProduct[]>;
};

function DeskSetup({ inventoryList }: Props) {
  const selectedItems = Object.values(inventoryList)
    .flat()
    .filter(({ selected }) => selected);

  const selectedItemsByCategory = Object.values(selectedItems).reduce(
    (products, { id, product }) => {
      return { ...products, [product.category]: { id, product } };
    },
    {}
  );

  const deskSetupItems = Object.entries(selectedItemsByCategory)
    .filter(([category]) => category !== 'software')
    .map(([_, item]) => item);

  return (
    <>
      {deskSetupItems.length > 0 ? (
        <S.GridContainer>
          {deskSetupItems.map((item: InventoryProduct, index) => (
            <DeskSetupCard
              key={index}
              item={item}
              size={'l'}
              borderType={'default'}
              index={index}
            />
          ))}
        </S.GridContainer>
      ) : (
        <S.NoContents>등록한 데스크 셋업이 없어요!</S.NoContents>
      )}
    </>
  );
}

export default DeskSetup;
