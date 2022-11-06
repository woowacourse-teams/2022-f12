import { AxiosRequestHeaders } from 'axios';
import { useContext } from 'react';

import { UserDataContext } from '@/contexts/LoginContextProvider';

import useAxios from '@/hooks/api/useAxios';
import useError from '@/hooks/useError';
import useModal from '@/hooks/useModal';

import { VALIDATION_ERROR_MESSAGES } from '@/constants/messages';

type Props = {
  url: (reviewId: number) => string;
  headers?: AxiosRequestHeaders;
};

function useDelete({ url, headers }: Props): (id: number) => Promise<void> {
  const { axiosInstance } = useAxios();
  const handleError = useError();
  const { showAlert } = useModal();
  const userData = useContext(UserDataContext);

  const deleteData = async (id: number) => {
    if (!userData || !userData.token) {
      await showAlert(VALIDATION_ERROR_MESSAGES.LOGIN_REQUIRED);
      return;
    }

    const { token } = userData;

    try {
      await axiosInstance.delete(url(id), {
        headers: { ...headers, Authorization: `Bearer ${token}` },
      });
    } catch (error) {
      await handleError(
        error as Error,
        `token: ${headers ? headers.Authorization.toString() : '없음'}`
      );
    }
  };

  return deleteData;
}
export default useDelete;
