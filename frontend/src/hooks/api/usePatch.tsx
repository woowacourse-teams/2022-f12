import { AxiosRequestHeaders, AxiosResponse } from 'axios';
import { useContext } from 'react';

import { SetUserDataContext, UserDataContext } from '@/contexts/LoginContextProvider';

import useAxios from '@/hooks/api/useAxios';
import useError from '@/hooks/useError';

type Props = {
  url: string;
  headers?: AxiosRequestHeaders;
};

function usePatch<T>({
  url,
  headers,
}: Props): (data: Record<string, unknown>) => Promise<AxiosResponse<T> | undefined> {
  const { axiosInstance } = useAxios();
  const handleError = useError();
  const userData = useContext(UserDataContext);
  const setUserData = useContext(SetUserDataContext);
  if (userData === null || setUserData === null) {
    throw new Error('컨텍스트 안에서 사용할 수 있습니다.');
  }

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
      await handleError(
        error as Error,
        `data: ${requestBodyString},\n    token: ${
          headers ? headers.Authorization.toString() : '없음'
        }`
      );
    }
  };

  return patchData;
}
export default usePatch;
