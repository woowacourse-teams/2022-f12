import { AxiosRequestHeaders } from 'axios';
import { useContext } from 'react';
import { UserDataContext } from '@/contexts/LoginContextProvider';
import handleError from '@/utils/handleError';
import useAxios from '@/hooks/api/useAxios';

type Props = {
  url: string;
  headers: null | AxiosRequestHeaders;
};

function usePost<T>({ url, headers }: Props): (input: T) => Promise<void> {
  const userData = useContext(UserDataContext);

  const { axiosInstance } = useAxios();

  const postData = async (body: T) => {
    if (!userData || !userData.token) {
      alert('로그인이 필요합니다.');
      return;
    }

    try {
      await axiosInstance.post(url, body, {
        headers,
      });
    } catch (error) {
      const requestBodyString = Object.entries(body).reduce<string>(
        (string, [key, value]) => `${string}\n${key}: ${value as string}`,
        ''
      );
      handleError(
        error as Error,
        `body: ${requestBodyString},\n    token: ${userData.token}`
      );
    }
  };

  return postData;
}

export default usePost;
