import { ENDPOINTS } from '@/constants/api';
import { UserDataContext } from '@/contexts/LoginContextProvider';
import useGetOne from '@/hooks/api/useGetOne';
import usePatch from '@/hooks/api/usePatch';
import { useContext, useEffect, useMemo, useState } from 'react';

type InventoryResponse = {
  items: InventoryProduct[];
};

type Return = {
  items: InventoryProduct[];
  isReady: boolean;
  isError: boolean;
  selectedProduct: InventoryProduct | null;
  setSelectedProduct: React.Dispatch<React.SetStateAction<InventoryProduct>>;
  otherProducts: InventoryProduct[];
  refetch: () => void;
  updateProfileProduct: () => Promise<boolean>;
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

  const [selectedProduct, setSelectedProduct] =
    useState<InventoryProduct | null>(null);
  const patchProfileProduct = usePatch({
    url: ENDPOINTS.INVENTORY_PRODUCTS,
    headers: { Authorization: `Bearer ${userData?.token}` },
  });

  const initialSelectedProduct = useMemo<InventoryProduct>(
    () => isReady && inventoryProducts.items.find(({ selected }) => selected),
    [inventoryProducts]
  );

  const updateProfileProduct = async () => {
    if (
      !selectedProduct ||
      (initialSelectedProduct &&
        selectedProduct.id === initialSelectedProduct.id)
    ) {
      return false;
    }

    const patchBody = { selectedInventoryProductId: selectedProduct.id };
    if (initialSelectedProduct) {
      patchBody['unselectedInventoryProductId'] = initialSelectedProduct.id;
    }
    await patchProfileProduct(patchBody);
    return true;
  };

  const otherProducts =
    isReady &&
    (selectedProduct
      ? inventoryProducts.items.filter(({ id }) => id !== selectedProduct.id)
      : inventoryProducts.items);

  useEffect(() => {
    if (!isReady) return;

    const newSelectedProduct = inventoryProducts.items.find(
      ({ selected }) => selected
    );

    setSelectedProduct(newSelectedProduct);
  }, [inventoryProducts]);

  return {
    items: inventoryProducts?.items,
    isReady,
    isError,
    selectedProduct,
    setSelectedProduct,
    otherProducts,
    refetch,
    updateProfileProduct,
  };
}

export default useInventory;
