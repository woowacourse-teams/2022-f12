import { AxiosRequestHeaders } from 'axios';

import useGetMany from '@/hooks/api/useGetMany';

type Props = {
  url: string;
  query: string;
  filter: Record<string, string>;
  size: string;
  headers?: AxiosRequestHeaders;
};

type Return<T> = DataFetchStatus & {
  result: T[];
  getNextPage: () => void;
  refetch: () => void;
  hasNextPage: boolean;
};

function useSearch<T>({ url, query, filter, size, headers }: Props): Return<T> {
  const params = { query, ...filter, size };
  const {
    data: result,
    hasNextPage,
    getNextPage,
    refetch,
    isLoading,
    isReady,
    isError,
  } = useGetMany<T>({
    url,
    params,
    headers,
  });

  return { result, refetch, hasNextPage, getNextPage, isLoading, isReady, isError };
}

export default useSearch;
