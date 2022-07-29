import { Player } from '@lottiefiles/react-lottie-player';

function Loading() {
  return (
    <>
      <Player
        autoplay
        loop
        src="https://assets6.lottiefiles.com/packages/lf20_l2jhcsuq.json"
        style={{ height: '100px', width: '100px' }}
      />
    </>
  );
}

export default Loading;
