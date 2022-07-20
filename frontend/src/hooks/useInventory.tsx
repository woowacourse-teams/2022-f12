import { ENDPOINTS } from '@/constants/api';
import {
  InventoryProductsContext,
  RefetchInventoryProductsContext,
} from '@/contexts/InventoryContextProvider';
import { UserDataContext } from '@/contexts/LoginContextProvider';
import usePatch from '@/hooks/api/usePatch';
import { useContext, useEffect, useMemo, useState } from 'react';

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
  const keyboards = useContext(InventoryProductsContext);
  const refetchInventoryProducts = useContext(RefetchInventoryProductsContext);

  const [selectedProduct, setSelectedProduct] =
    useState<InventoryProduct | null>(null);
  const patchProfileProduct = usePatch({
    url: ENDPOINTS.INVENTORY_PRODUCTS,
    headers: { Authorization: `Bearer ${token}` },
  });
  const initialSelectedProduct = useMemo<InventoryProduct>(
    () => keyboards && keyboards.find(({ selected }) => selected),
    [keyboards]
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

  const otherProducts = selectedProduct
    ? keyboards.filter(({ id }) => id !== selectedProduct.id)
    : keyboards;

  useEffect(() => {
    if (!keyboards) return;

    const newSelectedProduct = keyboards.find(({ selected }) => selected);

    setSelectedProduct(newSelectedProduct);
  }, [keyboards]);

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
