import { createContext, PropsWithChildren, useState } from 'react';

import Modal from '@/components/common/Modal/Modal';
import useAnimation from '@/hooks/useAnimation';

export const ShowAlertContext = createContext<(message: string) => Promise<void>>(null);
export const GetConfirmContext =
  createContext<(message: string) => Promise<boolean>>(null);

let resolveAlert: () => void;
let resolveConfirm: (value: boolean | PromiseLike<boolean>) => void;

function ModalContextProvider({ children }: PropsWithChildren) {
  const [message, setMessage] = useState('');
  const [alertOpen, setAlertOpen] = useState(false);
  const [confirmOpen, setConfirmOpen] = useState(false);

  const [shouldRenderAlert, handleAlertUnmount, alertAnimationTrigger] =
    useAnimation(alertOpen);
  const [shouldRenderConfirm, handleConfirmUnmount, confirmAnimationTrigger] =
    useAnimation(confirmOpen);

  const showAlert = (message: string): Promise<void> => {
    setMessage(message);
    setAlertOpen(true);

    return new Promise((resolve) => {
      resolveAlert = resolve;
    });
  };

  const handleAlertClose = () => {
    resolveAlert();
    setAlertOpen(false);
  };

  const showConfirm = (message: string) => {
    setMessage(message);
    setConfirmOpen(true);
  };

  const getConfirm: (message: string) => Promise<boolean> = (message) => {
    showConfirm(message);

    return new Promise((resolve) => {
      resolveConfirm = resolve;
    });
  };

  const handleConfirmClose = () => {
    resolveConfirm(false);
    setConfirmOpen(false);
  };

  const handleConfirm = () => {
    resolveConfirm(true);
    setConfirmOpen(false);
  };

  return (
    <ShowAlertContext.Provider value={showAlert}>
      <GetConfirmContext.Provider value={getConfirm}>
        {children}
        {shouldRenderAlert && (
          <Modal
            handleClose={handleAlertClose}
            handleUnmount={handleAlertUnmount}
            animationTrigger={alertAnimationTrigger}
          >
            <Modal.Body>{message}</Modal.Body>
          </Modal>
        )}
        {shouldRenderConfirm && (
          <Modal
            handleClose={handleConfirmClose}
            handleConfirm={handleConfirm}
            handleUnmount={handleConfirmUnmount}
            animationTrigger={confirmAnimationTrigger}
          >
            <Modal.Body>{message}</Modal.Body>
          </Modal>
        )}
      </GetConfirmContext.Provider>
    </ShowAlertContext.Provider>
  );
}

export default ModalContextProvider;
