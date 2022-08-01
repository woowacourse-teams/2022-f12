import {
  GetConfirmContext,
  ShowAlertContext,
} from '@/contexts/ModalContextProvider';
import { useContext } from 'react';

function useModal() {
  const showAlert = useContext(ShowAlertContext);
  const getConfirm = useContext(GetConfirmContext);
  return { showAlert, getConfirm };
}

export default useModal;
