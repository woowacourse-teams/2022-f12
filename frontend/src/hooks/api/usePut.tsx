import { AxiosRequestHeaders } from 'axios';
import axiosInstance from '@/hooks/api/axiosInstance';
import { useContext } from 'react';
import { UserDataContext } from '@/contexts/LoginContextProvider';

type Props = {
  url: string;
  headers: null | AxiosRequestHeaders;
};

function usePut<T>({
  url,
  headers,
}: Props): (input: T, id: number) => Promise<void> {
  const userData = useContext(UserDataContext);

  const putData = async (body: T, id: number) => {
    if (!userData || !userData.token) {
      alert('로그인이 필요합니다.');
      return;
    }

    await axiosInstance.put(`${url}/${id}`, body, {
      headers,
    });
  };

  return putData;
}

export default usePut;
