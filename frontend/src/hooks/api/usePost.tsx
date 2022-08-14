import { AxiosRequestHeaders } from 'axios';
import { useContext } from 'react';

import { UserDataContext } from '@/contexts/LoginContextProvider';

import useAxios from '@/hooks/api/useAxios';
import useError from '@/hooks/useError';
import useModal from '@/hooks/useModal';

import { VALIDATION_ERROR_MESSAGES } from '@/constants/messages';

type Props = {
  url: string;
  headers: null | AxiosRequestHeaders;
};

function usePost<T>({ url, headers }: Props): (input: T) => Promise<void> {
  const userData = useContext(UserDataContext);

  const { axiosInstance } = useAxios();
  const { showAlert } = useModal();
  const handleError = useError();

  const postData = async (body: T) => {
    if (!userData || !userData.token) {
      await showAlert(VALIDATION_ERROR_MESSAGES.LOGIN_REQUIRED);
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
      await handleError(
        error as Error,
        `body: ${requestBodyString},\n    token: ${userData.token}`
      );
    }
  };

  return postData;
}

export default usePost;
