import useGetMany from '@/hooks/api/useGetMany';
import { ENDPOINTS } from '@/constants/api';

type Sort = 'rating,desc' | 'reviewCount,desc';

type Props = {
  size: string;
  sort?: Sort;
  category?: string;
};

type ReturnType = {
  products: Product[];
  getNextPage: () => void;
  isLoading: boolean;
  isReady: boolean;
};

function useProducts({ size, sort, category }: Props): ReturnType {
  const params = { size, sort, category };
  const {
    data: products,
    getNextPage,
    isLoading,
    isReady,
  } = useGetMany<Product>({
    url: `${ENDPOINTS.PRODUCTS}`,
    params,
  });

  return { products, getNextPage, isLoading, isReady };
}

export default useProducts;
