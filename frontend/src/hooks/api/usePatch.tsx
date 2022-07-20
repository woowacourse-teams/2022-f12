import { AxiosRequestHeaders, AxiosResponse } from 'axios';
import axiosInstance from '@/hooks/api/axiosInstance';

type Props = {
  url: string;
  headers?: null | AxiosRequestHeaders;
};

function usePatch<T>({
  url,
  headers,
}: Props): (data: Record<string, unknown>) => Promise<AxiosResponse<T>> {
  const patchData = async (data: Record<string, unknown>) => {
    const response: AxiosResponse<T> = await axiosInstance.patch(url, data, {
      headers,
    });

    return response;
  };

  return patchData;
}
export default usePatch;
