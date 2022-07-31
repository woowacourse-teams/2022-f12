import { AxiosRequestHeaders, AxiosResponse } from 'axios';
import useAxios from '@/hooks/api/useAxios';
import useError from '@/hooks/useError';

type Props = {
  url: string;
  headers?: null | AxiosRequestHeaders;
};

function useGet<T>({
  url,
  headers,
}: Props): (params: Record<string, unknown>) => Promise<T> {
  const { axiosInstance } = useAxios();
  const handleError = useError();

  const fetchData = async (params: unknown) => {
    try {
      const { data }: AxiosResponse<T> = await axiosInstance.get(url, {
        headers,
        params,
      });
      return data;
    } catch (error) {
      handleError(error as Error);
    }
  };

  return fetchData;
}
export default useGet;
