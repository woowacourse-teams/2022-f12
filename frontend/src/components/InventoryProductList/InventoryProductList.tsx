import ProductBar from '@/components/common/ProductBar/ProductBar';
import * as S from '@/components/InventoryProductList/InventoryProductList.style';

type Props = {
  products: InventoryProduct[];
};

function InventoryProductList({ products }: Props) {
  return (
    <S.Container>
      {products.map(({ id: inventoryId, selected, product: { name } }) => (
        <ProductBar
          key={inventoryId}
          name={name}
          barType={selected ? 'selected' : 'default'}
        />
      ))}
    </S.Container>
  );
}

export default InventoryProductList;
