import { AxiosRequestHeaders } from 'axios';
import axiosInstance from '@/hooks/api/axiosInstance';

type Props = {
  url: string;
  headers?: null | AxiosRequestHeaders;
};

function usePut<T>({
  url,
  headers,
}: Props): (input: T, id: number) => Promise<void> {
  const putData = async (body: T, id: number) => {
    await axiosInstance.put(`${url}/${id}`, body, {
      headers,
    });
  };

  return putData;
}

export default usePut;
