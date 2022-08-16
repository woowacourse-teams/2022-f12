import { useContext } from 'react';

import { UserDataContext } from '@/contexts/LoginContextProvider';

import useGetOne from '@/hooks/api/useGetOne';
import usePatch from '@/hooks/api/usePatch';

import { ENDPOINTS } from '@/constants/api';

type InventoryResponse = {
  items: InventoryProduct[];
};

type Props = {
  memberId?: string;
};

type Return = Omit<DataFetchStatus, 'isLoading'> & {
  items: InventoryProduct[];
  refetch: () => void;
  updateProfileProduct: (ids: number[]) => Promise<boolean>;
};

function useInventory({ memberId }: Props): Return {
  const userData = useContext(UserDataContext);
  const {
    data: inventoryProducts,
    refetch,
    isReady,
    isError,
  } = useGetOne<InventoryResponse>({
    url: memberId
      ? ENDPOINTS.OTHER_INVENTORY_PRODUCTS(memberId)
      : ENDPOINTS.INVENTORY_PRODUCTS,
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
