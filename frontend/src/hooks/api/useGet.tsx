import { AxiosRequestHeaders, AxiosResponse } from 'axios';

import useAxios from '@/hooks/api/useAxios';
import useError from '@/hooks/useError';

type Props = {
  url: string;
  headers?: AxiosRequestHeaders;
};

type FetchDataArgs = {
  params?: Record<string, unknown>;
  token?: string;
  includeCookie?: boolean;
};

type FetchData<T> = ({
  params,
  token,
  includeCookie,
}?: FetchDataArgs) => Promise<T | undefined>;

function useGet<T>({ url, headers }: Props): FetchData<T> {
  const { axiosInstance } = useAxios();
  const handleError = useError();

  const fetchData: FetchData<T> = async (args) => {
    try {
      const { data }: AxiosResponse<T> = await axiosInstance.get(url, {
        headers:
          args && args.token
            ? { ...headers, Authorization: `Bearer ${args.token}` }
            : headers,
        params: args && args.params,
        withCredentials: (args && args.includeCookie) || false,
      });
      return data;
    } catch (error) {
      await handleError(error as Error);
    }
  };

  return fetchData;
}
export default useGet;
