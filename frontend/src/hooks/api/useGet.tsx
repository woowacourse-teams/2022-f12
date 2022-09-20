import { AxiosRequestHeaders, AxiosResponse } from 'axios';

import useAxios from '@/hooks/api/useAxios';
import useError from '@/hooks/useError';

type Props = {
  url: string;
  headers?: null | AxiosRequestHeaders;
};

type FetchDataArgs = { params?: Record<string, unknown>; token?: string } | undefined;

type FetchData<T> = ({ params, token }?: FetchDataArgs) => Promise<T>;

function useGet<T>({ url, headers }: Props): FetchData<T> {
  const { axiosInstance } = useAxios();
  const handleError = useError();

  const fetchData = async ({ params, token }: FetchDataArgs) => {
    try {
      const { data }: AxiosResponse<T> = await axiosInstance.get(url, {
        headers: token ? { ...headers, Authorization: `Bearer ${token}` } : headers,
        params,
      });
      return data;
    } catch (error) {
      await handleError(error as Error);
    }
  };

  return fetchData;
}
export default useGet;
