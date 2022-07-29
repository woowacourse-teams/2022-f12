import { ENDPOINTS } from '@/constants/api';
import { UserDataContext } from '@/contexts/LoginContextProvider';
import useGetOne from '@/hooks/api/useGetOne';
import usePatch from '@/hooks/api/usePatch';
import { useContext, useEffect, useMemo, useState } from 'react';

type InventoryResponse = {
  keyboards: InventoryProduct[];
};

type Return = {
  keyboards: InventoryProduct[];
  isReady: boolean;
  selectedProduct: InventoryProduct | null;
  setSelectedProduct: React.Dispatch<React.SetStateAction<InventoryProduct>>;
  otherProducts: InventoryProduct[];
  refetchInventoryProducts: () => void;
  updateProfileProduct: () => Promise<void>;
};

function useInventory(): Return {
  const userData = useContext(UserDataContext);
  const [inventoryProducts, refetchInventoryProducts, isReady] =
    useGetOne<InventoryResponse>({
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
    () =>
      isReady && inventoryProducts.keyboards.find(({ selected }) => selected),
    [inventoryProducts]
  );

  const updateProfileProduct = async () => {
    if (
      !selectedProduct ||
      (initialSelectedProduct &&
        selectedProduct.id === initialSelectedProduct.id)
    ) {
      return;
    }

    const patchBody = { selectedInventoryProductId: selectedProduct.id };
    if (initialSelectedProduct) {
      patchBody['unselectedInventoryProductId'] = initialSelectedProduct.id;
    }
    await patchProfileProduct(patchBody);
  };

  const otherProducts =
    isReady &&
    (selectedProduct
      ? inventoryProducts.keyboards.filter(
          ({ id }) => id !== selectedProduct.id
        )
      : inventoryProducts.keyboards);

  useEffect(() => {
    if (!isReady) return;

    const newSelectedProduct = inventoryProducts.keyboards.find(
      ({ selected }) => selected
    );

    setSelectedProduct(newSelectedProduct);
  }, [inventoryProducts]);

  return {
    keyboards: inventoryProducts?.keyboards,
    isReady,
    selectedProduct,
    setSelectedProduct,
    otherProducts,
    refetchInventoryProducts,
    updateProfileProduct,
  };
}

export default useInventory;
