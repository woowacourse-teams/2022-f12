import { Fragment } from 'react';
import { Link } from 'react-router-dom';

import DeskSetupCard from '@/components/DeskSetupCard/DeskSetupCard';
import * as S from '@/components/Profile/InventoryProductList/InventoryProductList.style';
import ProductBar from '@/components/Profile/ProductBar/ProductBar';

import { CATEGORIES } from '@/constants/product';
import ROUTES from '@/constants/routes';

type Props = {
  inventoryList: Record<string, InventoryProduct[]>;
};

function InventoryProductList({ inventoryList }: Props) {
  return (
    <>
      {Object.entries(inventoryList).map(([category, items]) => (
        <Fragment key={category}>
          <S.CategoryTitle>{CATEGORIES[category]}</S.CategoryTitle>
          <S.Container>
            {items.map(({ id: inventoryId, selected, product: { name } }) => (
              <Link key={inventoryId} to={`${ROUTES.PRODUCT}/${inventoryId}`}>
                <DeskSetupCard inventoryId={inventoryId} size="s" />
                {/* <ProductBar
                  key={inventoryId}
                  name={name}
                  barType={selected ? 'selected' : 'default'}
                /> */}
              </Link>
            ))}
          </S.Container>
        </Fragment>
      ))}
    </>
  );
}

export default InventoryProductList;
