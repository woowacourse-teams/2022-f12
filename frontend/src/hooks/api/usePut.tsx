import { AxiosRequestHeaders } from 'axios';
import { useContext } from 'react';

import { UserDataContext } from '@/contexts/LoginContextProvider';

import useAxios from '@/hooks/api/useAxios';
import useError from '@/hooks/useError';
import useModal from '@/hooks/useModal';

type Props = {
  url: string;
  headers: null | AxiosRequestHeaders;
};

function usePut<T>({
  url,
  headers,
}: Props): (input: T, id: number) => Promise<void> {
  const userData = useContext(UserDataContext);

  const { axiosInstance } = useAxios();
  const { showAlert } = useModal();
  const handleError = useError();

  const putData = async (body: T, id: number) => {
    if (!userData || !userData.token) {
      showAlert('로그인이 필요합니다.');
      return;
    }

    try {
      await axiosInstance.put(`${url}/${id}`, body, {
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

  return putData;
}

export default usePut;
