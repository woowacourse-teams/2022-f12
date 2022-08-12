import { Fragment } from 'react';
import { Link } from 'react-router-dom';

import ProductBar from '@/components/common/ProductBar/ProductBar';

import * as S from '@/components/InventoryProductList/InventoryProductList.style';

import ROUTES from '@/constants/routes';

const categories = {
  keyboard: '키보드',
  mouse: '마우스',
  monitor: '모니터',
  stand: '거치대',
  software: '소프트웨어',
} as const;

type Props = {
  inventoryList: Record<string, InventoryProduct[]>;
};

function InventoryProductList({ inventoryList }: Props) {
  return (
    <>
      {Object.entries(inventoryList).map(([category, items]) => (
        <Fragment key={category}>
          <S.CategoryTitle>{categories[category]}</S.CategoryTitle>
          <S.Container>
            {items.map(({ id: inventoryId, selected, product: { name } }) => (
              <Link key={inventoryId} to={`${ROUTES.PRODUCT}/${inventoryId}`}>
                <ProductBar
                  key={inventoryId}
                  name={name}
                  barType={selected ? 'selected' : 'default'}
                />
              </Link>
            ))}
          </S.Container>
        </Fragment>
      ))}
    </>
  );
}

export default InventoryProductList;
