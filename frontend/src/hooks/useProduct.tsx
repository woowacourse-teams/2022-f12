import useGetOne from '@/hooks/api/useGetOne';

import { ENDPOINTS } from '@/constants/api';

type Props = {
  id: number;
};

function useProduct({ id }: Props): [Product | null, boolean, boolean, () => void] {
  const {
    data: product,
    isReady,
    isError,
    refetch,
  } = useGetOne<Product>({
    url: `${ENDPOINTS.PRODUCT(id)}`,
  });

  return [product, isReady, isError, refetch];
}

export default useProduct;
