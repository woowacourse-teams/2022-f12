import { Player } from '@lottiefiles/react-lottie-player';

import LOTTIE_FILES from '@/constants/lottieFiles';

function ErrorPlaceholder() {
  return (
    <>
      <Player
        autoplay
        loop
        src={LOTTIE_FILES.ERROR}
        style={{ height: '200px', width: '200px' }}
      />
      <div style={{ width: '100%', textAlign: 'center' }}>오류가 발생했어요..</div>
    </>
  );
}

export default ErrorPlaceholder;
