import ReviewBottomSheet from '@/components/ReviewBottomSheet/ReviewBottomSheet';
import { useReducer, useRef } from 'react';
import styled from 'styled-components';

export default {
  component: ReviewBottomSheet,
  title: 'Components/ReviewBottomSheet',
};

const Container = styled.div`
  width: 375px;
  height: 600px;
  position: relative;

  border: 1px solid #000;
`;

const Template = (args) => {
  const [isOpen, toggleOpen] = useReducer((isOpen: boolean) => !isOpen, false);
  const containerRef = useRef();

  return (
    <Container ref={containerRef}>
      <button onClick={toggleOpen}>열기</button>
      {isOpen && <ReviewBottomSheet {...args} handleClose={toggleOpen} />}
    </Container>
  );
};

export const Default = () => <Template />;
