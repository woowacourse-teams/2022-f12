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
    (products, { product }) => {
      return { ...products, [product.category]: { product } };
    },
    {}
  );

  const deskSetupItems = Object.entries(selectedItemsByCategory)
    .filter(([category]) => category !== 'software')
    .map(([_, item]) => item);

  return (
    <>
      <S.Container>
        <S.CardWrapper>
          {deskSetupItems.length > 0 ? (
            deskSetupItems.map((item: InventoryProduct, index) => (
              <DeskSetupCard key={index} item={item} size={'l'} borderType={'default'} />
            ))
          ) : (
            <S.NoContents>데스크 셋업에 추가한 제품이 없어요</S.NoContents>
          )}
        </S.CardWrapper>
      </S.Container>
    </>
  );
}

export default DeskSetup;
