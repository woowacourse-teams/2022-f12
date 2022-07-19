import { ENDPOINTS } from '@/constants/api';
import { UserDataContext } from '@/contexts/LoginContextProvider';
import useGetOne from '@/hooks/api/useGetOne';
import usePatch from '@/hooks/api/usePatch';
import { useContext } from 'react';

type InventoryResponse = {
  keyboards: InventoryProduct[];
};

type Return = {
  keyboards: InventoryProduct[];
  selectedProduct: InventoryProduct | null;
  refetchInventoryProducts: () => void;
  updateProfileProduct: (
    selectedInventoryProductId: InventoryProduct['id'],
    unselectedInventoryProductId: InventoryProduct['id']
  ) => Promise<void>;
};

function useInventory(): Return {
  const [inventoryProducts, refetchInventoryProducts] =
    useGetOne<InventoryResponse>({
      url: ENDPOINTS.INVENTORY_PRODUCTS,
    });

  const { token } = useContext(UserDataContext);
  const patchProfileProduct = usePatch({
    url: ENDPOINTS.INVENTORY_PRODUCTS,
    headers: { Authorization: `Bearer ${token}` },
  });
  const keyboards = (inventoryProducts && inventoryProducts.keyboards) || [];
  const selectedProduct =
    inventoryProducts &&
    inventoryProducts.keyboards.find(({ selected }) => selected);
  const updateProfileProduct = async (
    selectedInventoryProductId: InventoryProduct['id'],
    unselectedInventoryProductId: InventoryProduct['id']
  ) => {
    if (
      selectedInventoryProductId === undefined &&
      unselectedInventoryProductId === undefined
    ) {
      throw new Error('유효하지 않은 요청입니다.');
    }
    if (selectedInventoryProductId === unselectedInventoryProductId) {
      return;
    }
    await patchProfileProduct({
      selectedInventoryProductId,
      unselectedInventoryProductId,
    });
  };

  return {
    keyboards,
    selectedProduct,
    refetchInventoryProducts,
    updateProfileProduct,
  };
}

export default useInventory;
