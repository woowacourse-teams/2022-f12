import logError from '@/utils/logError';
import { AxiosRequestHeaders, AxiosResponse } from 'axios';
import { useState, useEffect } from 'react';

import useAxios from '@/hooks/api/useAxios';
import useCache from '@/hooks/api/useCache';
import useModal from '@/hooks/useModal';

import { CACHE_TIME } from '@/constants/cache';

type Props = {
  url: string;
  headers?: null | AxiosRequestHeaders;
};

type Return<T> = Omit<DataFetchStatus, 'isLoading'> & {
  data: T;
  refetch: () => void;
};

function useGetOne<T>({ url, headers }: Props): Return<T> {
  const [data, setData] = useState<null | T>(null);
  const [refetchTrigger, setRefetchTrigger] = useState(0);
  const { axiosInstance, isLoading, isError } = useAxios();
  const { showAlert } = useModal();
  const { getWithCache, removeCache } = useCache();

  const fetchData = async () => {
    const { data }: AxiosResponse<T> = (await getWithCache({
      axiosInstance,
      url,
      config: {
        headers,
      },
      maxAge: CACHE_TIME.THREE_MINS,
    })) as AxiosResponse<T>;

    return data;
  };
  const refetch = () => {
    setRefetchTrigger((prevValue) => prevValue + 1);
    removeCache(url);
  };

  const getErrorStateMessage = () => {
    return `@useGetMany\n상태:\n    url: ${url}\n    refetch#: ${refetchTrigger}`;
  };

  useEffect(() => {
    fetchData()
      .then((data) => {
        !!data && setData(data);
      })
      .catch(async (error: Error) => {
        logError(error, getErrorStateMessage());
        await showAlert(error.message);
      });
  }, [refetchTrigger]);

  const isReady = !isLoading && !!data;

  return { data, refetch, isReady, isError };
}
export default useGetOne;
