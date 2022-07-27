import useGetMany from '@/hooks/api/useGetMany';
import { ENDPOINTS } from '@/constants/api';

type Sort = 'default' | 'rating,desc' | 'reviewCount,desc';

type Props = {
  size: number;
  sort?: Sort;
  category?: string;
};

type ReturnType = [Product[], () => void];

function useProducts({ size, sort, category }: Props): ReturnType {
  const [products, getNextPage] = useGetMany<Product>({
    url: `${ENDPOINTS.PRODUCTS}`,
    size,
    sort: sort === 'default' ? undefined : sort,
    category,
  });

  return [products, getNextPage];
}

export default useProducts;
