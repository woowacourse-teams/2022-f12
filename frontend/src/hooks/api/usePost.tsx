import { AxiosRequestHeaders } from 'axios';
import { useContext } from 'react';

import { UserDataContext } from '@/contexts/LoginContextProvider';

import useAxios from '@/hooks/api/useAxios';
import useError from '@/hooks/useError';
import useModal from '@/hooks/useModal';

import { VALIDATION_ERROR_MESSAGES } from '@/constants/messages';

type Props = {
  url: string;
  headers?: AxiosRequestHeaders;
};

function usePost<T>({ url, headers }: Props): (input: T) => Promise<void> {
  const userData = useContext(UserDataContext);

  const { axiosInstance } = useAxios();
  const { showAlert } = useModal();
  const handleError = useError();

  const postData = async (body: T) => {
    if (!userData || !userData.token) {
      await showAlert(VALIDATION_ERROR_MESSAGES.LOGIN_REQUIRED);
      throw new Error(VALIDATION_ERROR_MESSAGES.LOGIN_REQUIRED);
    }

    const { token } = userData;

    try {
      await axiosInstance.post(url, body, {
        headers: { ...headers, Authorization: `Bearer ${token}` },
      });
    } catch (error) {
      const requestBodyString = Object.entries(body).reduce<string>(
        (string, [key, value]) => `${string}\n${key}: ${value as string}`,
        ''
      );

      await handleError(
        error as Error,
        `body: ${requestBodyString},\n    token: ${token}`
      );
    }
  };

  return postData;
}

export default usePost;
