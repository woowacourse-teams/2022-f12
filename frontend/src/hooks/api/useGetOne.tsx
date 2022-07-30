import { AxiosRequestHeaders, AxiosResponse } from 'axios';
import { useState, useEffect } from 'react';
import useAxios from '@/hooks/api/useAxios';
import logError from '@/utils/logError';

type Props = {
  url: string;
  headers?: null | AxiosRequestHeaders;
};

type Return<T> = {
  data: T;
  refetch: () => void;
  isReady: boolean;
  isError: boolean;
};

function useGetOne<T>({ url, headers }: Props): Return<T> {
  const [data, setData] = useState<null | T>(null);
  const [refetchTrigger, setRefetchTrigger] = useState(0);
  const { axiosInstance, isLoading, isError } = useAxios();
  const fetchData = async () => {
    const { data }: AxiosResponse<T> = await axiosInstance.get(url, {
      headers,
    });

    return data;
  };
  const refetch = () => {
    setRefetchTrigger((prevValue) => prevValue + 1);
  };

  const getErrorStateMessage = () => {
    return `@useGetMany\n상태:\n    url: ${url}\n    refetch#: ${refetchTrigger}`;
  };

  useEffect(() => {
    fetchData()
      .then((data) => {
        !!data && setData(data);
      })
      .catch((error: Error) => {
        logError(error, getErrorStateMessage());
        alert('사용자에게 표시할 오류 메시지');
      });
  }, [refetchTrigger]);

  const isReady = !isLoading && !!data;

  return { data, refetch, isReady, isError };
}
export default useGetOne;
