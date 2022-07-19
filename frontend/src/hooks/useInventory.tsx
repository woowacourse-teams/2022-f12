import { ENDPOINTS } from '@/constants/api';
import { UserDataContext } from '@/contexts/LoginContextProvider';
import useGetOne from '@/hooks/api/useGetOne';
import usePatch from '@/hooks/api/usePatch';
import { useContext, useEffect, useRef, useState } from 'react';

type InventoryResponse = {
  keyboards: InventoryProduct[];
};

type Return = {
  keyboards: InventoryProduct[];
  selectedProduct: InventoryProduct | null;
  setSelectedProduct: React.Dispatch<React.SetStateAction<InventoryProduct>>;
  otherProducts: InventoryProduct[];
  refetchInventoryProducts: () => void;
  updateProfileProduct: (
    newSelectedInventoryProductId: InventoryProduct['id']
  ) => Promise<void>;
};

function useInventory(): Return {
  const [inventoryProducts, refetchInventoryProducts] =
    useGetOne<InventoryResponse>({
      url: ENDPOINTS.INVENTORY_PRODUCTS,
    });
  const { token } = useContext(UserDataContext);
  const [selectedProduct, setSelectedProduct] =
    useState<InventoryProduct | null>(null);
  const patchProfileProduct = usePatch({
    url: ENDPOINTS.INVENTORY_PRODUCTS,
    headers: { Authorization: `Bearer ${token}` },
  });
  const initialSelectedProduct = useRef<InventoryProduct>();

  const keyboards = (inventoryProducts && inventoryProducts.keyboards) || [];

  const updateProfileProduct = async (
    newSelectedInventoryProductId: InventoryProduct['id']
  ) => {
    if (
      newSelectedInventoryProductId === undefined &&
      initialSelectedProduct.current.id === undefined
    ) {
      throw new Error('유효하지 않은 요청입니다.');
    }
    if (newSelectedInventoryProductId === initialSelectedProduct.current.id) {
      return;
    }
    await patchProfileProduct({
      selectedInventoryProductId: newSelectedInventoryProductId,
      unselectedInventoryProductId: initialSelectedProduct.current.id,
    });
  };

  const otherProducts =
    selectedProduct && keyboards.filter(({ id }) => id !== selectedProduct.id);

  useEffect(() => {
    if (!inventoryProducts) return;

    const newSelectedProduct = inventoryProducts.keyboards.find(
      ({ selected }) => selected
    );

    if (!selectedProduct && !!inventoryProducts) {
      initialSelectedProduct.current = newSelectedProduct;
    }

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
