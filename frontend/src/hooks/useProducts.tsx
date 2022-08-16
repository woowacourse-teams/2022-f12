import useGetMany from '@/hooks/api/useGetMany';

import { ENDPOINTS } from '@/constants/api';

type Sort = 'rating,desc' | 'reviewCount,desc';

type Props = {
  size: string;
  sort?: Sort;
  category?: Category;
};

type Return = DataFetchStatus & {
  products: Product[];
  getNextPage: () => void;
};

function useProducts({ size, sort, category }: Props): Return {
  const params = { size, sort, category };
  const {
    data: products,
    getNextPage,
    isLoading,
    isReady,
    isError,
  } = useGetMany<Product>({
    url: `${ENDPOINTS.PRODUCTS}`,
    params,
  });

  return { products, getNextPage, isLoading, isReady, isError };
}

export default useProducts;
