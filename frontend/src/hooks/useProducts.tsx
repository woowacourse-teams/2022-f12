import useGetMany from './api/useGetMany';
import { ENDPOINTS } from '../constants/api';

type Sort = 'default' | 'rating' | 'reviewCount';

type Props = {
  size: number;
  sort?: Sort;
};

type ReturnType = [Product[], () => void];

function useProducts({ size, sort }: Props): ReturnType {
  const [products, getNextPage] = useGetMany<Product>({
    url: `${ENDPOINTS.PRODUCTS}`,
    size,
    sort,
  });

  return [products, getNextPage];
}

export default useProducts;
