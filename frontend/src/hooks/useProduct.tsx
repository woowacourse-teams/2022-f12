import useGetOne from './api/useGetOne';
import { ENDPOINTS } from '../constants/api';

type Product = {
  id: number;
  name: string;
  imageUrl: string;
  rating: number;
};

type Props = {
  productId: number;
};

function useProduct({ productId }: Props): Product {
  const product = useGetOne<Product>({
    url: `${ENDPOINTS.PRODUCT(productId)}`,
  });

  return product;
}

export default useProduct;
