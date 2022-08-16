import { useContext } from 'react';

import { GetConfirmContext, ShowAlertContext } from '@/contexts/ModalContextProvider';

function useModal() {
  const showAlert = useContext(ShowAlertContext);
  const getConfirm = useContext(GetConfirmContext);
  return { showAlert, getConfirm };
}

export default useModal;
