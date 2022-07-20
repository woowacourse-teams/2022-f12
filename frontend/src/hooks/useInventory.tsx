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
  selectedProduct: InventoryProduct | null;
  setSelectedProduct: React.Dispatch<React.SetStateAction<InventoryProduct>>;
  otherProducts: InventoryProduct[];
  refetchInventoryProducts: () => void;
  updateProfileProduct: () => Promise<void>;
};

function useInventory(): Return {
  const { token } = useContext(UserDataContext);
  const [inventoryProducts, refetchInventoryProducts] =
    useGetOne<InventoryResponse>({
      url: ENDPOINTS.INVENTORY_PRODUCTS,
      headers: { Authorization: `Bearer ${token}` },
    });
  const [selectedProduct, setSelectedProduct] =
    useState<InventoryProduct | null>(null);
  const patchProfileProduct = usePatch({
    url: ENDPOINTS.INVENTORY_PRODUCTS,
    headers: { Authorization: `Bearer ${token}` },
  });
  const initialSelectedProduct = useMemo<InventoryProduct>(
    () =>
      inventoryProducts &&
      inventoryProducts.keyboards.find(({ selected }) => selected),
    [inventoryProducts]
  );

  const keyboards = (inventoryProducts && inventoryProducts.keyboards) || [];

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

  const otherProducts = selectedProduct
    ? keyboards.filter(({ id }) => id !== selectedProduct.id)
    : keyboards;

  useEffect(() => {
    if (!inventoryProducts) return;

    const newSelectedProduct = inventoryProducts.keyboards.find(
      ({ selected }) => selected
    );

    setSelectedProduct(newSelectedProduct);
  }, [inventoryProducts]);

  return {
    keyboards,
    selectedProduct,
    setSelectedProduct,
    otherProducts,
    refetchInventoryProducts,
    updateProfileProduct,
  };
}

export default useInventory;
