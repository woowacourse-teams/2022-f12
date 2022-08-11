import { AxiosRequestHeaders, AxiosResponse } from 'axios';
import { useContext } from 'react';

import {
  SetUserDataContext,
  UserDataContext,
} from '@/contexts/LoginContextProvider';

import useAxios from '@/hooks/api/useAxios';
import useError from '@/hooks/useError';

type Props = {
  url: string;
  headers?: null | AxiosRequestHeaders;
};

function usePatch<T>({
  url,
  headers,
}: Props): (data: Record<string, unknown>) => Promise<AxiosResponse<T>> {
  const { axiosInstance } = useAxios();
  const handleError = useError();
  const userData = useContext(UserDataContext);
  const setUserData = useContext(SetUserDataContext);

  const patchData = async (data: Record<string, unknown>) => {
    try {
      const response: AxiosResponse<T> = await axiosInstance.patch(url, data, {
        headers,
      });

      const newUserData = { ...userData };
      newUserData.member = { ...newUserData.member, ...data };
      newUserData.registerCompleted = true;

      setUserData(newUserData);
      return response;
    } catch (error) {
      const requestBodyString = Object.entries(data).reduce<string>(
        (string, [key, value]) => `${string}\n${key}: ${value as string}`,
        ''
      );
      handleError(
        error as Error,
        `data: ${requestBodyString},\n    token: ${headers.Authorization.toString()}`
      );
    }
  };

  return patchData;
}
export default usePatch;
