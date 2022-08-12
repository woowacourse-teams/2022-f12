import logError from '@/utils/logError';

import useModal from '@/hooks/useModal';

function useError() {
  const { showAlert } = useModal();

  const handleError = async (error: Error, additionalMessage?: string) => {
    if (!(error instanceof Error)) {
      await showAlert('알 수 없는 오류 발생');
      console.log(error);
      throw new Error('API 요청에서 오류 발생');
    }

    logError(error, additionalMessage);
    await showAlert('사용자에게 표시할 오류 메시지');
    throw new Error('API 요청에서 오류 발생');
  };

  return handleError;
}

export default useError;
