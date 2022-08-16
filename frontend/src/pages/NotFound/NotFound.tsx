import { Player } from '@lottiefiles/react-lottie-player';
import { useNavigate } from 'react-router-dom';

import * as S from '@/pages/NotFound/NotFound.style';

import LOTTIE_FILES from '@/constants/lottieFiles';
import ROUTES from '@/constants/routes';

function NotFound() {
  const navigate = useNavigate();

  return (
    <S.Container>
      <Player
        autoplay
        loop
        src={LOTTIE_FILES.NOT_FOUND}
        style={{ height: '300px', width: '300px' }}
      />
      <S.Description style={{ width: '100%', textAlign: 'center' }}>
        존재하지 않는 페이지입니다!
      </S.Description>
      <S.ButtonContainer>
        <S.NavButton
          onClick={() => {
            navigate(ROUTES.HOME);
          }}
        >
          홈으로
        </S.NavButton>
        <S.NavButton
          onClick={() => {
            navigate(-1);
          }}
        >
          이전 페이지로
        </S.NavButton>
      </S.ButtonContainer>
    </S.Container>
  );
}

export default NotFound;
