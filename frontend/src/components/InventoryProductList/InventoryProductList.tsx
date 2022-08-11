import { Link } from 'react-router-dom';

import ProductBar from '@/components/common/ProductBar/ProductBar';

import * as S from '@/components/InventoryProductList/InventoryProductList.style';

import ROUTES from '@/constants/routes';

type Props = {
  products: InventoryProduct[];
};

function InventoryProductList({ products }: Props) {
  return (
    <S.Container>
      {products.map(({ id: inventoryId, selected, product: { name } }) => (
        <Link key={inventoryId} to={`${ROUTES.PRODUCT}/${inventoryId}`}>
          <ProductBar
            key={inventoryId}
            name={name}
            barType={selected ? 'selected' : 'default'}
          />
        </Link>
      ))}
    </S.Container>
  );
}

export default InventoryProductList;
