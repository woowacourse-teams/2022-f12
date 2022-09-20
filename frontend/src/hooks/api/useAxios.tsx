import axios, { AxiosError, AxiosInstance } from 'axios';
import { useContext, useState } from 'react';

import { LogoutContext } from '@/contexts/LoginContextProvider';

import { BASE_URL } from '@/constants/api';
import {
  API_ERROR_CODE_EXCEPTION_MESSAGES,
  API_ERROR_MESSAGES,
  FAILURE_MESSAGES,
} from '@/constants/messages';

type ErrorResponseBody = {
  errorCode: keyof typeof API_ERROR_MESSAGES;
};

type Return = Omit<DataFetchStatus, 'isReady'> & {
  axiosInstance: AxiosInstance;
};

function useAxios(): Return {
  const [isLoading, setLoading] = useState(false);
  const [isError, setError] = useState(false);
  const logout = useContext(LogoutContext);

  const axiosInstance = axios.create({ baseURL: BASE_URL });

  const handleAPIError = (error: AxiosError<ErrorResponseBody>) => {
    const errorResponseBody = error.response.data;

    setError(true);
    setLoading(false);

    if (!errorResponseBody || !('errorCode' in errorResponseBody)) {
      throw new Error(API_ERROR_CODE_EXCEPTION_MESSAGES.NO_CODE);
    }

    if (errorResponseBody.errorCode === 40105) {
      throw new Error(FAILURE_MESSAGES.NO_REFRESH_TOKEN);
    }

    if (error.response.status === 401) {
      // TODO: access token 만료 오류 코드 일 시에 retry 하는 작업 추가
      // 원래 요청 설정은 error.config에 들어가있음
      logout();
    }

    const { errorCode } = errorResponseBody;

    throw new Error(
      API_ERROR_MESSAGES[errorCode] ?? API_ERROR_CODE_EXCEPTION_MESSAGES.UNKNOWN
    );
  };

  axiosInstance.interceptors.request.use((request) => {
    setError(false);
    setLoading(true);
    return request;
  });

  axiosInstance.interceptors.response.use((response) => {
    setLoading(false);
    return response;
  }, handleAPIError);

  return { axiosInstance, isLoading, isError };
}

export default useAxios;
