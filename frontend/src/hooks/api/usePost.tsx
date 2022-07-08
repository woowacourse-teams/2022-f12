import { AxiosRequestHeaders } from 'axios';
import axiosInstance from '@/hooks/api/axiosInstance';

type Props = {
  url: string;
  headers?: null | AxiosRequestHeaders;
};

function usePost<T>({ url, headers }: Props): (input: T) => Promise<void> {
  const postData = async (body: T) => {
    await axiosInstance.post(url, body, {
      headers,
    });
  };

  return postData;
}

export default usePost;
