import axios, { AxiosError, AxiosInstance } from 'axios';
import { useContext, useState } from 'react';

import { LogoutContext, SetUserDataContext } from '@/contexts/LoginContextProvider';

import { BASE_URL, ENDPOINTS } from '@/constants/api';
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
  const setUserData = useContext(SetUserDataContext);

  const axiosInstance = axios.create({ baseURL: BASE_URL });

  const reissueTokenAndRetry = async (error: AxiosError<ErrorResponseBody>) => {
    const originalConfig = error.config;
    const {
      data: { accessToken: token },
    } = await axios.post<{ accessToken: string }>(
      `${BASE_URL}${ENDPOINTS.ISSUE_ACCESS_TOKEN}`
    );

    setUserData((prev) => ({ ...prev, token }));

    return await axiosInstance({
      ...originalConfig,
      headers: { ...originalConfig.headers, Authorization: `Bearer ${token}` },
    });
  };

  const handleAPIError = async (error: AxiosError<ErrorResponseBody>) => {
    if (error.response === undefined) {
      throw new Error(FAILURE_MESSAGES.NO_REQUEST_MADE);
    }

    const errorResponseBody = error.response.data;

    // setError보다 먼저 와야 오류 화면이 표시되지 않음
    if (errorResponseBody.errorCode === '40104') {
      return await reissueTokenAndRetry(error);
    }

    setError(true);
    setLoading(false);

    if (!errorResponseBody || !('errorCode' in errorResponseBody)) {
      throw new Error(API_ERROR_CODE_EXCEPTION_MESSAGES.NO_CODE);
    }

    if (errorResponseBody.errorCode === '40105') {
      throw new Error(FAILURE_MESSAGES.NO_REFRESH_TOKEN);
    }

    if (error.response.status === 401) {
      logout();
    }

    throw new Error(
      API_ERROR_MESSAGES[errorResponseBody.errorCode] ??
        API_ERROR_CODE_EXCEPTION_MESSAGES.UNKNOWN
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
