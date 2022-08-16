import { Player } from '@lottiefiles/react-lottie-player';
import styled from 'styled-components';

import LOTTIE_FILES from '@/constants/lottieFiles';

const Container = styled.section`
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
`;

function Loading() {
  return (
    <Container aria-label={'loading-indicator'}>
      <Player
        autoplay
        loop
        src={LOTTIE_FILES.LOADING}
        style={{ height: '100px', width: '100px' }}
      />
    </Container>
  );
}

export default Loading;
