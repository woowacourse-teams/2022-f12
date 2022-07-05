import axios, { AxiosRequestHeaders } from 'axios';

type Props = {
  url: string;
  headers?: null | AxiosRequestHeaders;
};

function usePost<T>({ url, headers }: Props): (T) => Promise<void> {
  const postData = async (body: T) => {
    await axios.post(url, {
      headers,
      data: body,
    });
  };

  return postData;
}

export default usePost;
