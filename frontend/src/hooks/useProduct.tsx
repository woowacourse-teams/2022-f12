import useGetOne from '@/hooks/api/useGetOne';
import { ENDPOINTS } from '@/constants/api';

type Props = {
  productId: number;
};

function useProduct({ productId }: Props): [Product, boolean] {
  const [product, _, isReady] = useGetOne<Product>({
    url: `${ENDPOINTS.PRODUCT(productId)}`,
  });

  return [product, isReady];
}

export default useProduct;
