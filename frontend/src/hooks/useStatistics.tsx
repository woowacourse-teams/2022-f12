import { ENDPOINTS } from '@/constants/api';
import useGetOne from '@/hooks/api/useGetOne';

type Props = {
  productId: number;
};

function useStatistics({ productId }: Props): [Statistics, boolean, boolean] {
  const {
    data: statistics,
    isReady,
    isError,
  } = useGetOne<Statistics>({
    url: `${ENDPOINTS.PRODUCT(productId)}/statistics`,
  });

  return [statistics, isReady, isError];
}

export default useStatistics;
