import styled from 'styled-components';

import F12Load from '@/assets/F12LOAD.gif';

const Container = styled.section`
  width: 100%;
  height: 25rem;
  display: flex;
  justify-content: center;
  align-items: center;

  img {
    width: 150px;
  }
`;

function Loading() {
  return (
    <Container aria-label={'loading-indicator'}>
      <img src={F12Load} alt="" />
    </Container>
  );
}

export default Loading;
