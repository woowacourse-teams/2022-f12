import useGetMany from '@/hooks/api/useGetMany';
import { ENDPOINTS } from '@/constants/api';

type Sort = 'rating,desc' | 'reviewCount,desc';

type Props = {
  size: string;
  sort?: Sort;
  category?: string;
};

type ReturnType = [Product[], () => void];

function useProducts({ size, sort, category }: Props): ReturnType {
  const params = { size, sort, category };
  const [products, getNextPage] = useGetMany<Product>({
    url: `${ENDPOINTS.PRODUCTS}`,
    params,
  });

  return [products, getNextPage];
}

export default useProducts;
