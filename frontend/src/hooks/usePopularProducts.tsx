import useGetMany from '@/hooks/api/useGetMany';

import { ENDPOINTS } from '@/constants/api';

type Props = {
  size: string;
};

type Return = DataFetchStatus & {
  products: Product[] | null;
};

function usePopularProducts({ size }: Props): Return {
  const {
    data: products,
    isLoading,
    isReady,
    isError,
  } = useGetMany<Product>({
    url: `${ENDPOINTS.POPULAR_PRODUCTS}`,
    params: {
      size,
    },
  });

  return { products, isLoading, isReady, isError };
}

export default usePopularProducts;
