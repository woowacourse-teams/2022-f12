import { ENDPOINTS } from '@/constants/api';
import useGetOne from '@/hooks/api/useGetOne';

type Prop = {
  memberId: string;
};

type InventoryResponse = {
  items: InventoryProduct[];
};

type Return = {
  items: InventoryProduct[];
  isReady: boolean;
  isError: boolean;
};

function useOtherInventory({ memberId }: Prop): Return {
  const {
    data: inventoryProducts,
    isReady,
    isError,
  } = useGetOne<InventoryResponse>({
    url: ENDPOINTS.OTHER_INVENTORY_PRODUCTS(memberId),
  });
  console.log(isError);

  return {
    items: inventoryProducts?.items,
    isReady,
    isError,
  };
}

export default useOtherInventory;
