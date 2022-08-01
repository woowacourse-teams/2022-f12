import useModal from '@/hooks/useModal';
import logError from '@/utils/logError';

function useError() {
  const { showAlert } = useModal();

  const handleError = (error: Error, additionalMessage?: string) => {
    if (!(error instanceof Error)) {
      showAlert('알 수 없는 오류 발생');
      console.log(error);
    }

    logError(error, additionalMessage);
    showAlert('사용자에게 표시할 오류 메시지');
  };

  return handleError;
}

export default useError;