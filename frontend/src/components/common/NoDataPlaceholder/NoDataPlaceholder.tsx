import { Player } from '@lottiefiles/react-lottie-player';

function NoDataPlaceholder() {
  return (
    <>
      <Player
        autoplay
        loop
        src="https://assets1.lottiefiles.com/private_files/lf30_oqpbtola.json"
        style={{ height: '200px', width: '200px' }}
      />
      <div style={{ width: '100%', textAlign: 'center' }}>아무것도 찾지 못했어요..</div>
    </>
  );
}

export default NoDataPlaceholder;
