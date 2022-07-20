import { ENDPOINTS } from '@/constants/api';
import { UserDataContext } from '@/contexts/LoginContextProvider';
import useGetOne from '@/hooks/api/useGetOne';
import { createContext, PropsWithChildren, useContext } from 'react';

type InventoryResponse = {
  keyboards: InventoryProduct[];
};

export const InventoryProductsContext = createContext<InventoryProduct[]>([]);
export const RefetchInventoryProductsContext = createContext<() => void>(null);

function InventoryContextProvider({ children }: PropsWithChildren) {
  const { token } = useContext(UserDataContext);
  const [inventoryProducts, refetchInventoryProducts] =
    useGetOne<InventoryResponse>({
      url: ENDPOINTS.INVENTORY_PRODUCTS,
      headers: { Authorization: `Bearer ${token}` },
    });

  return (
    <InventoryProductsContext.Provider
      value={inventoryProducts ? inventoryProducts.keyboards : []}
    >
      <RefetchInventoryProductsContext.Provider
        value={refetchInventoryProducts}
      >
        {children}
      </RefetchInventoryProductsContext.Provider>
    </InventoryProductsContext.Provider>
  );
}

export default InventoryContextProvider;
