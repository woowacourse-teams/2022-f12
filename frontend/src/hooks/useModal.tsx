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
  return { showAlert, getConfirm, showReview };
}

export default useModal;
