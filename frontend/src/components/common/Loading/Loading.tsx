import { Player } from '@lottiefiles/react-lottie-player';
import styled from 'styled-components';

const Container = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
`;

function Loading() {
  return (
    <Container>
      <Player
        autoplay
        loop
        src="https://assets6.lottiefiles.com/packages/lf20_l2jhcsuq.json"
        style={{ height: '100px', width: '100px' }}
      />
    </Container>
  );
}

export default Loading;
