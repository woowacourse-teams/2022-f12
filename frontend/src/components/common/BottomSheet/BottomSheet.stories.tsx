import { useReducer, useRef } from 'react';
import styled from 'styled-components';

import BottomSheet from '@/components/common/BottomSheet/BottomSheet';

export default {
  component: BottomSheet,
  title: 'Components/Common/BottomSheet',
};

const Container = styled.div`
  width: 375px;
  height: 600px;
  position: relative;

  border: 1px solid #000;
`;

const Template = () => {
  const [isOpen, toggleOpen] = useReducer((isOpen: boolean) => !isOpen, false);
  const containerRef = useRef(null);

  return (
    <Container ref={containerRef}>
      <button onClick={toggleOpen}>열기</button>
      {isOpen && <BottomSheet handleClose={toggleOpen}>te</BottomSheet>}
    </Container>
  );
};

export const Default = () => <Template />;
