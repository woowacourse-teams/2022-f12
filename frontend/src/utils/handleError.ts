import logError from '@/utils/logError';

const handleError = (error: Error, additionalMessage?: string) => {
  if (!(error instanceof Error)) {
    alert('알 수 없는 오류 발생');
    console.log(error);
  }

  logError(error, additionalMessage);
  alert('사용자에게 표시할 오류 메시지');
};

export default handleError;
