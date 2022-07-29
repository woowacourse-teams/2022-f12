import { BASE_URL } from '@/constants/api';
import {
  API_ERROR_CODE_EXCEPTION_MESSAGES,
  API_ERROR_MESSAGES,
} from '@/constants/messages';
import axios, { AxiosError, AxiosInstance } from 'axios';
import { useState } from 'react';

type ErrorResponseBody = {
  errorCode: keyof typeof API_ERROR_MESSAGES;
};

function useAxios(): [AxiosInstance, boolean] {
  const [isLoading, setLoading] = useState(false);

  const axiosInstance = axios.create({ baseURL: BASE_URL });

  const handleAPIError = (error: AxiosError<ErrorResponseBody>) => {
    const errorResponseBody = error.response.data;
    if (!('errorCode' in errorResponseBody)) {
      throw new Error(API_ERROR_CODE_EXCEPTION_MESSAGES.NO_CODE);
    }

    const { errorCode } = errorResponseBody;

    throw new Error(
      API_ERROR_MESSAGES[errorCode] ?? API_ERROR_CODE_EXCEPTION_MESSAGES.UNKNOWN
    );
  };

  axiosInstance.interceptors.request.use((request) => {
    setLoading(true);
    return request;
  });

  axiosInstance.interceptors.response.use((response) => {
    setLoading(false);
    return response;
  }, handleAPIError);

  return [axiosInstance, isLoading];
}

export default useAxios;
