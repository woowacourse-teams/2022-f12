import useGetOne from './api/useGetOne';

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
  const product = useGetOne<Product>({ url: `/api/v1/keyboards/${productId}` });

  return product;
}

export default useProduct;
