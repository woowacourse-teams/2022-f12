import styled from 'styled-components';

import loadingGif from '@/assets/loading.gif';
import loadingWebp from '@/assets/loading.webp';

const Container = styled.section`
  width: 100%;
  height: 20rem;
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
      <picture>
        <source srcSet={loadingWebp} type="image/webP" />
        <img src={loadingGif} alt="" />
      </picture>
    </Container>
  );
}

export default Loading;
