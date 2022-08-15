import { Player } from '@lottiefiles/react-lottie-player';

import LOTTIE_FILES from '@/constants/lottieFiles';

function NoDataPlaceholder() {
  return (
    <>
      <Player
        autoplay
        loop
        src={LOTTIE_FILES.EMPTY_BOX}
        style={{ height: '200px', width: '200px' }}
      />
      <div style={{ width: '100%', textAlign: 'center' }}>아무것도 찾지 못했어요..</div>
    </>
  );
}

export default NoDataPlaceholder;
