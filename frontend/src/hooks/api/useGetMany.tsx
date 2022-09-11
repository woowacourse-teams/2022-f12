import logError from '@/utils/logError';
import { AxiosRequestHeaders, AxiosResponse } from 'axios';
import { useState, useEffect } from 'react';

import useAxios from '@/hooks/api/useAxios';
import useCache from '@/hooks/api/useCache';
import useModal from '@/hooks/useModal';

type SearchParams = Record<string, string>;

type GetManyParams = SearchParams & { size: string };

type Props = {
  url: string;
  params?: GetManyParams;
  body?: null | object;
  headers?: null | AxiosRequestHeaders;
};

type Data<T> = {
  hasNext: boolean;
  items: T[];
};

type Return<T> = DataFetchStatus & {
  data: T[];
  getNextPage: () => void;
  refetch: () => void;
};

function useGetMany<T>({ url, params, body, headers }: Props): Return<T> {
  const [data, setData] = useState<T[]>(null);

  const [page, setPage] = useState(0);
  const [hasNextPage, setHasNextPage] = useState(true);

  const [nextPageTrigger, setNextPageTrigger] = useState(0);
  const [refetchTrigger, setRefetchTrigger] = useState(0);

  const { axiosInstance, isLoading, isError } = useAxios();
  const { showAlert } = useModal();
  const { getWithCache, removeCache } = useCache();

  const searchParams = new URLSearchParams(params);

  const setDataFromResponse = (items: T[]) => {
    !!items && setData((prevData) => (prevData ? [...prevData, ...items] : items));
  };

  const setNextPageData = (hasNext: boolean) => {
    if (hasNext) {
      setPage((prevPage) => prevPage + 1);
    } else {
      setHasNextPage(false);
    }
  };

  const fetchData = async () => {
    const {
      data: { hasNext, items },
    } = (await getWithCache({
      axiosInstance,
      url,
      config: {
        data: body,
        headers,
        params: {
          page,
          ...params,
        },
      },
    })) as AxiosResponse<Data<T>>;

    setDataFromResponse(items);
    setNextPageData(hasNext);
  };

  const getNextPage = () => {
    setNextPageTrigger((prevTrigger) => prevTrigger + 1);
  };

  const refetch = () => {
    setRefetchTrigger((prevTrigger) => prevTrigger + 1);
    setPage(0);
    setHasNextPage(true);
    setData(null);
    removeCache(`${url}${searchParams.toString()}`);
  };

  const getCurrentParamString = () =>
    Object.entries(params)
      .map(([key, value]) => `${key}: ${value}`)
      .join('\n    ');

  const getErrorStateMessage = () => {
    return `@useGetMany\n상태:\n    url: ${url}\n    ${getCurrentParamString()}\n    page: ${page}`;
  };

  useEffect(() => {
    if (!hasNextPage || isLoading) return;

    fetchData().catch(async (error: Error) => {
      logError(error, getErrorStateMessage());
      await showAlert(error.message);
    });
  }, [nextPageTrigger, refetchTrigger]);

  useEffect(() => {
    if (!data) return; // 최초 렌더링 시 refetch 방지 임시 조치
    refetch();
  }, [searchParams.toString()]);

  const isReady = !!data;

  return { data, getNextPage, refetch, isLoading, isReady, isError };
}
export default useGetMany;
