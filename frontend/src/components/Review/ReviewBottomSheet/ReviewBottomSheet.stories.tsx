import { useReducer, useRef, useState } from 'react';
import styled from 'styled-components';

import ReviewBottomSheet from '@/components/Review/ReviewBottomSheet/ReviewBottomSheet';

export default {
  component: ReviewBottomSheet,
  title: 'Components/Review/ReviewBottomSheet',
};

const Container = styled.div`
  width: 375px;
  height: 600px;
  position: relative;

  border: 1px solid #000;
`;

const Template = () => {
  const [isOpen, setOpen] = useState(false);
  const containerRef = useRef();

  const handleSheetOpen = () => {
    setOpen(true);
  };

  const handleSheetClose = () => {
    setOpen(false);
  };
  const handleSheetSubmit = async (_: ReviewInput) => {
    await new Promise(() => {
      console.log();
    });
    return handleSheetClose();
  };
  const handleSheetEdit = async (_: ReviewInput, id: number) => {
    await new Promise(() => {
      console.log();
    });
    return handleSheetClose();
  };

  return (
    <Container ref={containerRef}>
      <button onClick={handleSheetOpen}>열기</button>
      {isOpen && (
        <ReviewBottomSheet
          handleClose={handleSheetClose}
          isEdit={false}
          handleSubmit={handleSheetSubmit}
          handleEdit={undefined}
          handleFocus={() => {
            console.log('focused');
          }}
        />
      )}
    </Container>
  );
};

export const Default = () => <Template />;
