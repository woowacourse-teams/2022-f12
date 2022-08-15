import logError from '@/utils/logError';

import useModal from '@/hooks/useModal';

import { API_ERROR_CODE_EXCEPTION_MESSAGES } from '@/constants/messages';

function useError() {
  const { showAlert } = useModal();

  const handleError = async (error: Error, additionalMessage?: string) => {
    if (!(error instanceof Error)) {
      await showAlert(API_ERROR_CODE_EXCEPTION_MESSAGES.UNKNOWN);
      console.log(error);
      throw new Error('API 요청에서 오류 발생');
    }

    logError(error, additionalMessage);
    await showAlert(error.message);
    throw new Error('API 요청에서 오류 발생');
  };

  return handleError;
}

export default useError;
