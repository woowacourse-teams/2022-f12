import { AxiosRequestHeaders, AxiosResponse } from 'axios';
import axiosInstance from '@/hooks/api/axiosInstance';

type Props = {
  url: string;
  headers?: null | AxiosRequestHeaders;
};

function useGet<T>({
  url,
  headers,
}: Props): (params: Record<string, unknown>) => Promise<T> {
  const fetchData = async (params: unknown) => {
    const { data }: AxiosResponse<T> = await axiosInstance.get(url, {
      headers,
      params,
    });

    return data;
  };

  return fetchData;
}
export default useGet;
