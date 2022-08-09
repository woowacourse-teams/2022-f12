import Modal from '@/components/common/Modal/Modal';
import { createContext, PropsWithChildren, useState } from 'react';

export const ShowAlertContext = createContext<(message: string) => void>(null);
export const GetConfirmContext =
  createContext<(message: string) => Promise<boolean>>(null);

let resolveConfirm: (value: boolean | PromiseLike<boolean>) => void;

function ModalContextProvider({ children }: PropsWithChildren) {
  const [message, setMessage] = useState('');
  const [alertOpen, setAlertOpen] = useState(false);
  const [confirmOpen, setConfirmOpen] = useState(false);

  const showAlert = (message: string) => {
    setMessage(message);
    setAlertOpen(true);
  };

  const handleAlertClose = () => {
    setAlertOpen(false);
  };

  const showConfirm = (message: string) => {
    setMessage(message);
    setConfirmOpen(true);
  };

  const handleConfirmClose = () => {
    resolveConfirm(false);
    setConfirmOpen(false);
  };

  const handleConfirm = () => {
    resolveConfirm(true);
    setConfirmOpen(false);
  };

  const getConfirm: (message: string) => Promise<boolean> = (message) => {
    showConfirm(message);

    return new Promise((resolve) => {
      resolveConfirm = resolve;
    });
  };

  return (
    <ShowAlertContext.Provider value={showAlert}>
      <GetConfirmContext.Provider value={getConfirm}>
        {children}
        {alertOpen && (
          <Modal handleClose={handleAlertClose}>
            <Modal.Body>{message}</Modal.Body>
          </Modal>
        )}
        {confirmOpen && (
          <Modal handleClose={handleConfirmClose} handleConfirm={handleConfirm}>
            <Modal.Body>{message}</Modal.Body>
          </Modal>
        )}
      </GetConfirmContext.Provider>
    </ShowAlertContext.Provider>
  );
}

export default ModalContextProvider;
