import useGetOne from '@/hooks/api/useGetOne';

import { ENDPOINTS } from '@/constants/api';

type Props = {
  productId: number;
};

function useProduct({ productId }: Props): [Product, boolean, boolean] {
  const {
    data: product,
    isReady,
    isError,
  } = useGetOne<Product>({
    url: `${ENDPOINTS.PRODUCT(productId)}`,
  });

  return [product, isReady, isError];
}

export default useProduct;
