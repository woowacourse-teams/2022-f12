import { AxiosRequestHeaders } from 'axios';
import axiosInstance from './axiosInstance';

type Props = {
  url: string;
  headers?: null | AxiosRequestHeaders;
};

function usePost<T>({ url, headers }: Props): (input: T) => Promise<void> {
  const postData = async (body: T) => {
    await axiosInstance.post(url, {
      headers,
      data: body,
    });
  };

  return postData;
}

export default usePost;
