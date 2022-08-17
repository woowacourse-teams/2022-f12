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
      return { ...products, [product.category]: product };
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
          {deskSetupItems.map((item: Product) => (
            <DeskSetupCard key={item.id} item={item} size={'l'} borderType={'default'} />
          ))}
        </S.CardWrapper>
      </S.Container>
    </>
  );
}

export default DeskSetup;
