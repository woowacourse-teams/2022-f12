import { ENDPOINTS } from '@/constants/api';
import useGetOne from '@/hooks/api/useGetOne';

type InventoryResponse = {
  keyboards: InventoryProduct[];
};

function useInventory() {
  // 인벤토리 상품 상태
  const data = useGetOne<InventoryResponse>({
    url: ENDPOINTS.INVENTORY_PRODUCTS,
  });
  // 프로필 상품 상태
  // 프로필 상품 변경 매서드

  return [data ? data.keyboards : []];
}

export default useInventory;
