import useGetMany from './api/useGetMany';
import { BASE_URL, ENDPOINTS } from '../constants/api';

type Product = {
  id: number;
  name: string;
  imageUrl: string;
  rating: number;
};

type Sort = 'default' | 'rating' | 'reviewCount';

type Props = {
  size: number;
  sort?: Sort;
};

type ReturnType = [Product[], () => void];

function useProducts({ size, sort }: Props): ReturnType {
  const [products, getNextPage] = useGetMany<Product>({
    url: `${BASE_URL}${ENDPOINTS.PRODUCTS}`,
    size,
    sort,
  });

  return [products, getNextPage];
}

export default useProducts;
