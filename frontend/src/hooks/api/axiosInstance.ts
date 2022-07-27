import axios, { AxiosError } from 'axios';
import { BASE_URL } from '@/constants/api';
import {
  API_ERROR_CODE_EXCEPTION_MESSAGES,
  API_ERROR_MESSAGES,
} from '@/constants/messages';

const axiosInstance = axios.create({ baseURL: BASE_URL });

type ErrorResponseBody = {
  errorCode: keyof typeof API_ERROR_MESSAGES;
};

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

axiosInstance.interceptors.response.use((response) => response, handleAPIError);

export default axiosInstance;
