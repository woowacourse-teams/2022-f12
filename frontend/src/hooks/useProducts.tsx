import useGetMany from './api/useGetMany';

type Product = {
  id: number;
  name: string;
  imageUrl: string;
  rating: number;
};

type Sort = 'default' | 'rating' | 'reviewCount';

type Props = {
  size: number;
  sort: Sort;
};

type ReturnType = [Product[], () => void];

function useProducts({ size, sort }: Props): ReturnType {
  const [products, getNextPage] = useGetMany<Product>({
    url: '/api/v1/keyboards',
    size,
    sort,
  });

  return [products, getNextPage];
}

export default useProducts;
