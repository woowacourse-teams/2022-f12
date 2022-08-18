import { createContext, PropsWithChildren, useState } from 'react';

import Modal from '@/components/common/Modal/Modal';
import Rating from '@/components/common/Rating/Rating';

import useAnimation from '@/hooks/useAnimation';

export const ShowAlertContext = createContext<(message: string) => Promise<void>>(null);
export const GetConfirmContext =
  createContext<(message: string) => Promise<boolean>>(null);
export const ShowReviewContext =
  createContext<(content: string, rating: number, createdAt: string) => Promise<void>>(
    null
  );

let resolveAlert: () => void;
let resolveConfirm: (value: boolean | PromiseLike<boolean>) => void;
let resolveReview: () => void;

function ModalContextProvider({ children }: PropsWithChildren) {
  const [message, setMessage] = useState('');
  const [review, setReview] = useState<{
    content: string;
    createdAt: string;
    rating: number;
  }>({
    content: null,
    createdAt: null,
    rating: null,
  });
  const [alertOpen, setAlertOpen] = useState(false);
  const [confirmOpen, setConfirmOpen] = useState(false);
  const [reviewOpen, setReviewOpen] = useState(false);

  const [shouldRenderAlert, handleAlertUnmount, alertAnimationTrigger] =
    useAnimation(alertOpen);
  const [shouldRenderConfirm, handleConfirmUnmount, confirmAnimationTrigger] =
    useAnimation(confirmOpen);
  const [shouldRenderReview, handleReviewUnmount, reviewAnimationTrigger] =
    useAnimation(reviewOpen);

  const dateFormatter = (createdAt: string) => {
    const createdAtDate = new Date(createdAt);

    return `${createdAtDate.getFullYear()}년 ${
      createdAtDate.getMonth() + 1
    }월 ${createdAtDate.getDate()}일`;
  };

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

  const showReview = (
    content: string,
    rating: number,
    createdAt: string
  ): Promise<void> => {
    setReview({ content, rating, createdAt });
    setReviewOpen(true);

    return new Promise((resolve) => {
      resolveReview = resolve;
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

  const handleReviewClose = () => {
    resolveReview();
    setReviewOpen(false);
  };

  return (
    <ShowAlertContext.Provider value={showAlert}>
      <GetConfirmContext.Provider value={getConfirm}>
        <ShowReviewContext.Provider value={showReview}>
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
          {shouldRenderReview && (
            <Modal
              handleClose={handleReviewClose}
              handleUnmount={handleReviewUnmount}
              animationTrigger={reviewAnimationTrigger}
            >
              <Modal.Body>
                <Rating rating={review.rating} type={'정수'} size={'small'} />
                <div>{dateFormatter(review.createdAt)}</div>
                {review.content}
              </Modal.Body>
            </Modal>
          )}
        </ShowReviewContext.Provider>
      </GetConfirmContext.Provider>
    </ShowAlertContext.Provider>
  );
}

export default ModalContextProvider;
