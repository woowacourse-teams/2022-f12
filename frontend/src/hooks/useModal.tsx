import { useContext } from 'react';

import {
  GetConfirmContext,
  ShowAlertContext,
  ShowReviewContext,
} from '@/contexts/ModalContextProvider';

function useModal() {
  const showAlert = useContext(ShowAlertContext);
  const getConfirm = useContext(GetConfirmContext);
  const showReview = useContext(ShowReviewContext);

  if (showAlert === null || getConfirm === null || showReview === null) {
    throw new Error('모달 컨텍스트 내부에서만 모달 훅을 이용할 수 있습니다.');
  }
  return { showAlert, getConfirm, showReview };
}

export default useModal;
