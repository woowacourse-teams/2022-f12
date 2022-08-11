import { PropsWithChildren, useState } from 'react';

import Modal from '@/components/common/Modal/Modal';

export default {
  component: Modal,
  title: 'Components/Modal',
};

type Props = {
  showConfirm: boolean;
};

const Template = ({ showConfirm, children }: PropsWithChildren<Props>) => {
  const [show, setShow] = useState(false);

  const handleClose = () => {
    setShow(false);
  };
  const handleSubmit = () => {
    alert('제출됨');
    handleClose();
  };
  return (
    <>
      <button onClick={() => setShow(true)}>표시하기</button>
      {show && (
        <Modal
          handleClose={handleClose}
          handleConfirm={showConfirm && handleSubmit}
        >
          {children}
        </Modal>
      )}
    </>
  );
};

export const Default = () => (
  <Template showConfirm={true}>
    <Modal.Title>제목</Modal.Title>
    <Modal.Body>
      Lorem ipsum, dolor sit amet consectetur adipisicing elit. Dolorum eveniet
      sint magnam nemo? Laboriosam animi non error veritatis, illo temporibus
      dolorum omnis alias repellat aperiam.
    </Modal.Body>
  </Template>
);

export const NoTitle = () => (
  <Template showConfirm={true}>
    <Modal.Body>
      Lorem ipsum, dolor sit amet consectetur adipisicing elit. Dolorum eveniet
      sint magnam nemo? Laboriosam animi non error veritatis, illo temporibus
      dolorum omnis alias repellat aperiam. A illum doloremque voluptas modi
      eaque iste voluptate ullam corrupti quibusdam id fugiat, maiores
      reprehenderit labore ipsam nemo, aliquam cumque facere nostrum libero fuga
      unde.
    </Modal.Body>
  </Template>
);

export const NoConfirm = () => (
  <Template showConfirm={false}>
    <Modal.Title>제목</Modal.Title>
    <Modal.Body>
      Lorem ipsum, dolor sit amet consectetur adipisicing elit. Dolorum eveniet
      sint magnam nemo? Laboriosam animi non error veritatis, illo temporibus
      dolorum omnis alias repellat aperiam. A illum doloremque voluptas modi
      eaque iste voluptate ullam corrupti quibusdam id fugiat, maiores
      reprehenderit labore ipsam nemo, aliquam cumque facere nostrum libero fuga
      unde.
    </Modal.Body>
  </Template>
);

export const NoTitleAndConfirm = () => (
  <Template showConfirm={false}>
    <Modal.Body>
      Lorem ipsum, dolor sit amet consectetur adipisicing elit. Dolorum eveniet
      sint magnam nemo? Laboriosam animi non error veritatis, illo temporibus
      dolorum omnis alias repellat aperiam. A illum doloremque voluptas modi
      eaque iste voluptate ullam corrupti quibusdam id fugiat, maiores
      reprehenderit labore ipsam nemo, aliquam cumque facere nostrum libero fuga
      unde.
    </Modal.Body>
  </Template>
);
