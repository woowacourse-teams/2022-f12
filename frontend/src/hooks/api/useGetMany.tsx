import logError from '@/utils/logError';
import { AxiosRequestHeaders, AxiosResponse } from 'axios';
import { useState, useEffect } from 'react';

import useAxios from '@/hooks/api/useAxios';
import usePagedCache from '@/hooks/api/usePagedCache';
import useModal from '@/hooks/useModal';

type SearchParams = Record<string, string>;

type GetManyParams = SearchParams & { size: string };

type Props = {
  url: string;
  params?: GetManyParams;
  body?: null | object;
  headers?: AxiosRequestHeaders;
};

type Data<T> = {
  hasNext: boolean;
  items: T[];
};

type Return<T> = DataFetchStatus & {
  data: T[] | null;
  getNextPage: () => void;
  refetch: () => void;
  hasNextPage: boolean;
};

function useGetMany<T>({ url, params, body, headers }: Props): Return<T> {
  const [data, setData] = useState<T[] | null>(null);

  const [page, setPage] = useState(0);
  const [hasNextPage, setHasNextPage] = useState(true);

  const [nextPageTrigger, setNextPageTrigger] = useState(0);
  const [refetchTrigger, setRefetchTrigger] = useState(0);

  const { axiosInstance, isLoading, isError } = useAxios();
  const { showAlert } = useModal();
  const { getWithCache, removeCache } = usePagedCache();

  const searchParams = new URLSearchParams(params);

  const setNextPageData = (hasNext: boolean) => {
    if (hasNext) {
      setPage((prevPage) => prevPage + 1);
    } else {
      setHasNextPage(false);
    }
  };

  const handleDataResponse = (responses: AxiosResponse<Data<T>>[]) => {
    const dataArray = responses.map(({ data: { items } }) => items).flat();
    const { hasNext } = responses.at(-1).data;

    return { dataArray, hasNext };
  };

  const fetchData = async () => {
    const responses = (await getWithCache({
      key: `${url}?${new URLSearchParams(params).toString()}`,
      page,
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
    })) as AxiosResponse<Data<T>>[];

    const { dataArray, hasNext } = handleDataResponse(responses);

    setData(dataArray);
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
    removeCache(`${url}?${searchParams.toString()}`);
  };

  const getCurrentParamString = (params: Record<string, string>) =>
    Object.entries(params)
      .map(([key, value]) => `${key}: ${value}`)
      .join('\n    ');

  const getErrorStateMessage = () => {
    return `@useGetMany\n상태:\n    url: ${url}\n    ${
      params ? getCurrentParamString(params) : ''
    }\n    page: ${page}`;
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

  return { data, hasNextPage, getNextPage, refetch, isLoading, isReady, isError };
}
export default useGetMany;
