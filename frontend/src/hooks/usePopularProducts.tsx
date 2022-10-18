import useGetMany from '@/hooks/api/useGetMany';

import { ENDPOINTS } from '@/constants/api';

type Props = {
  size: string;
};

type Return = DataFetchStatus & {
  products: Product[];
};

function usePopularProducts({ size }: Props): Return {
  const params = { size, page: null };

  const {
    data: products,
    isLoading,
    isReady,
    isError,
  } = useGetMany<Product>({
    url: `${ENDPOINTS.POPULAR_PRODUCTS}`,
    params,
  });

  return { products, isLoading, isReady, isError };
}

export default usePopularProducts;
