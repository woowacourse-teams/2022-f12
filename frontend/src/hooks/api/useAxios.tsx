import axios, { AxiosError, AxiosInstance } from 'axios';
import { useContext, useState } from 'react';

import { LogoutContext } from '@/contexts/LoginContextProvider';

import { BASE_URL } from '@/constants/api';
import {
  API_ERROR_CODE_EXCEPTION_MESSAGES,
  API_ERROR_MESSAGES,
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

    if (error.response.status === 401) {
      logout();
    }

    if (!('errorCode' in errorResponseBody)) {
      throw new Error(API_ERROR_CODE_EXCEPTION_MESSAGES.NO_CODE);
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
