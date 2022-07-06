import useGetOne from './api/useGetOne';
import { ENDPOINTS } from '../constants/api';

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
