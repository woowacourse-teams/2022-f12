import { useContext } from 'react';

import { UserDataContext } from '@/contexts/LoginContextProvider';

import useGetMany from '@/hooks/api/useGetMany';
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
  reviews: Review[];
  isReviewReady: boolean;
  isReviewLoading: boolean;
  isReviewError: boolean;
  refetch: () => void;
  updateProfileProduct: (ids: number[]) => Promise<boolean>;
  getNextPage: () => void;
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
      ? ENDPOINTS.OTHER_INVENTORY_PRODUCTS(Number(memberId))
      : ENDPOINTS.INVENTORY_PRODUCTS,
    headers: { Authorization: `Bearer ${userData?.token}` },
  });

  const hasToken = userData && userData.token !== undefined;

  const CommonParams = {
    params: {
      size: '4',
    },
  };

  const ParamsWithMemberId = {
    ...CommonParams,
    url: ENDPOINTS.REVIEWS_BY_MEMBER_ID(Number(memberId)),
  };

  const ParamsWithoutMemberId = {
    ...CommonParams,
    url: ENDPOINTS.MY_REVIEWS,
    headers: hasToken ? { Authorization: `Bearer ${userData.token}` } : null,
  };

  const {
    data: reviews,
    getNextPage,
    isLoading: isReviewLoading,
    isReady: isReviewReady,
    isError: isReviewError,
  } = useGetMany<Review>(
    memberId === undefined ? ParamsWithoutMemberId : ParamsWithMemberId
  );

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
    reviews,
    isReviewReady,
    isReviewLoading,
    isReviewError,
    getNextPage,
  };
}

export default useInventory;
