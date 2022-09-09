import { useNavigate } from 'react-router-dom';

import * as S from '@/pages/NotFound/NotFound.style';

import ROUTES from '@/constants/routes';

import notFoundGif from '@/assets/404.gif';
import notFoundWebP from '@/assets/404.webp';

function NotFound() {
  const navigate = useNavigate();

  return (
    <S.Container>
      <picture style={{ width: '100%', display: 'flex', justifyContent: 'center' }}>
        <source srcSet={notFoundWebP} type="image/webP" />
        <img src={notFoundGif} alt="" width={300} />
      </picture>
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
