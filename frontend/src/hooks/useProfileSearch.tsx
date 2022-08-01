import useGetMany from '@/hooks/api/useGetMany';
import { ENDPOINTS } from '@/constants/api';

type Props = {
  query: string;
  careerLevel: string;
  jobType: string;
  size: string;
};

type ReturnType = {
  profiles: ProfileSearchResult[];
  isLoading: boolean;
  isReady: boolean;
  isError: boolean;
  getNextPage: () => void;
  refetch: () => void;
};

function useProfileSearch({
  query,
  careerLevel,
  jobType,
  size,
}: Props): ReturnType {
  const params = { query, careerLevel, jobType, size };
  const {
    data: profiles,
    getNextPage,
    refetch,
    isLoading,
    isReady,
    isError,
  } = useGetMany<ProfileSearchResult>({
    url: `${ENDPOINTS.MEMBERS}`,
    params,
  });

  return { profiles, refetch, getNextPage, isLoading, isReady, isError };
}

export default useProfileSearch;
