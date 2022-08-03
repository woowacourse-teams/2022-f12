import { ENDPOINTS } from '@/constants/api';
import { UserDataContext } from '@/contexts/LoginContextProvider';
import useGetOne from '@/hooks/api/useGetOne';
import usePatch from '@/hooks/api/usePatch';
import { useContext } from 'react';

type InventoryResponse = {
  items: InventoryProduct[];
};

type Return = {
  items: InventoryProduct[];
  isReady: boolean;
  isError: boolean;
  refetch: () => void;
  updateProfileProduct: (ids: number[]) => Promise<boolean>;
};

function useInventory(): Return {
  const userData = useContext(UserDataContext);
  const {
    data: inventoryProducts,
    refetch,
    isReady,
    isError,
  } = useGetOne<InventoryResponse>({
    url: ENDPOINTS.INVENTORY_PRODUCTS,
    headers: { Authorization: `Bearer ${userData?.token}` },
  });

  const patchProfileProduct = usePatch({
    url: ENDPOINTS.INVENTORY_PRODUCTS,
    headers: { Authorization: `Bearer ${userData?.token}` },
  });

  const updateProfileProduct = async (selectedProductArray: number[]) => {
    const patchBody = { selectedInventoryProductIds: selectedProductArray };

    await patchProfileProduct(patchBody);
    return true;
  };

  return {
    items: inventoryProducts?.items,
    isReady,
    isError,
    refetch,
    updateProfileProduct,
  };
}

export default useInventory;
