import DeskSetupCard from '@/components//DeskSetupCard/DeskSetupCard';
import * as S from '@/components/DeskSetup/DeskSetup.style';

import GitHubIcon from '@/assets/github.svg';

function DeskSetup() {
  return (
    <>
      <S.Container>
        <S.CardWrapper>
          <DeskSetupCard size={'l'} />
          <DeskSetupCard size={'l'} />
          <DeskSetupCard size={'l'} />
          <DeskSetupCard size={'l'} />
        </S.CardWrapper>
      </S.Container>
      <S.GitHubWrapper>
        <S.GitHubId>@hamcheeseburger</S.GitHubId>
        <S.GitHubLink>
          <GitHubIcon />
        </S.GitHubLink>
      </S.GitHubWrapper>
    </>
  );
}

export default DeskSetup;
