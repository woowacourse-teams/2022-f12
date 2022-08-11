import useGetMany from '@/hooks/api/useGetMany';

type Props = {
  url: string;
  query: string;
  filter: Record<string, string>;
  size: string;
};

type ReturnType<T> = {
  result: T[];
  isLoading: boolean;
  isReady: boolean;
  isError: boolean;
  getNextPage: () => void;
  refetch: () => void;
};

function useSearch<T>({ url, query, filter, size }: Props): ReturnType<T> {
  const params = { query, ...filter, size };
  const {
    data: result,
    getNextPage,
    refetch,
    isLoading,
    isReady,
    isError,
  } = useGetMany<T>({
    url,
    params,
  });

  return { result, refetch, getNextPage, isLoading, isReady, isError };
}

export default useSearch;
