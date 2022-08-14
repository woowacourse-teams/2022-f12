import useGetOne from '@/hooks/api/useGetOne';

import { ENDPOINTS } from '@/constants/api';

type Props = {
  productId: Product['id'];
};

function useStatistics({ productId }: Props): [Statistics, boolean, boolean, () => void] {
  const {
    data: statistics,
    isReady,
    isError,
    refetch,
  } = useGetOne<Statistics>({
    url: `${ENDPOINTS.PRODUCT(productId)}/statistics`,
  });

  return [statistics, isReady, isError, refetch];
}

export default useStatistics;
