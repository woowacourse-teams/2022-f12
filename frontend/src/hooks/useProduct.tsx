import useGetOne from '@/hooks/api/useGetOne';

import { ENDPOINTS } from '@/constants/api';

type Props = {
  id: number;
};

function useProduct({ id }: Props): [Product, boolean, boolean] {
  const {
    data: product,
    isReady,
    isError,
  } = useGetOne<Product>({
    url: `${ENDPOINTS.PRODUCT(id)}`,
  });

  return [product, isReady, isError];
}

export default useProduct;
