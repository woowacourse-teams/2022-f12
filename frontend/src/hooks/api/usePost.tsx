import { AxiosRequestHeaders } from 'axios';
import { useContext } from 'react';

import { UserDataContext } from '@/contexts/LoginContextProvider';

import useAxios from '@/hooks/api/useAxios';
import useError from '@/hooks/useError';
import useModal from '@/hooks/useModal';

import { FAILURE_MESSAGES, VALIDATION_ERROR_MESSAGES } from '@/constants/messages';

type Props = {
  url: string;
  headers?: AxiosRequestHeaders;
};

function usePost<T, U>({
  url,
  headers,
}: Props): (input?: T, includeToken?: boolean) => Promise<U>;
function usePost<T>({
  url,
  headers,
}: Props): (input?: T, includeToken?: boolean) => Promise<void>;
function usePost<T, U>({
  url,
  headers,
}: Props): (input?: T, includeToken?: boolean) => Promise<void> | Promise<U> {
  const userData = useContext(UserDataContext);

  const { axiosInstance } = useAxios();
  const { showAlert } = useModal();
  const handleError = useError();

  const isError = (error: unknown): error is Error => {
    return error instanceof Error;
  };

  const postData = async (body: T, includeToken = true) => {
    if (includeToken && (!userData || !userData.token)) {
      await showAlert(VALIDATION_ERROR_MESSAGES.LOGIN_REQUIRED);
      throw new Error(VALIDATION_ERROR_MESSAGES.LOGIN_REQUIRED);
    }

    try {
      const response = await axiosInstance.post<U>(url, body, {
        headers: {
          ...headers,
          Authorization: includeToken && `Bearer ${userData.token}`,
        },
        withCredentials: true,
      });

      if (response && response.data) return response.data;
      return;
    } catch (error) {
      if (isError(error) && error.message === FAILURE_MESSAGES.NO_REFRESH_TOKEN) {
        throw new Error(FAILURE_MESSAGES.NO_REFRESH_TOKEN);
      }

      await handleError(error as Error);
    }
  };

  return postData;
}

export default usePost;
