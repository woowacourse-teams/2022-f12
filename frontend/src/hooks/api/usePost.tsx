import { AxiosRequestHeaders } from 'axios';
import axiosInstance from '@/hooks/api/axiosInstance';
import { useContext } from 'react';
import { UserDataContext } from '@/contexts/LoginContextProvider';

type Props = {
  url: string;
  headers: null | AxiosRequestHeaders;
};

function usePost<T>({ url, headers }: Props): (input: T) => Promise<void> {
  const userData = useContext(UserDataContext);

  const postData = async (body: T) => {
    if (!userData || !userData.token) {
      alert('로그인이 필요합니다.');
      return;
    }
    await axiosInstance.post(url, body, {
      headers,
    });
  };

  return postData;
}

export default usePost;
