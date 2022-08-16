import Chip from '@/components/common/Chip/Chip';

import * as S from '@/components/Profile/UserInfo/UserInfo.style';

import { GITHUB_URL } from '@/constants/link';
import { CAREER_LEVELS, JOB_TYPES } from '@/constants/profile';

import GithubIcon from '@/assets/github.svg';

type Props = {
  userData: Member;
};

function UserInfo({ userData }: Props) {
  const { imageUrl, gitHubId, jobType, careerLevel } = userData;
  return (
    <S.Container>
      <S.ImageWrapper>
        <S.ProfileImage src={imageUrl} alt="" />
      </S.ImageWrapper>
      <S.UserNameWrapper>
        <S.Username>{`@${gitHubId}`}</S.Username>
        <S.GithubLink
          href={`${GITHUB_URL}${gitHubId}`}
          target="_blank"
          rel="noopener noreferrer"
        >
          <GithubIcon />
        </S.GithubLink>
      </S.UserNameWrapper>
      {
        <S.ChipWrapper>
          <Chip size="l">{JOB_TYPES[jobType]}</Chip>
          <Chip size="l">{CAREER_LEVELS[careerLevel]}</Chip>
        </S.ChipWrapper>
      }
    </S.Container>
  );
}

export default UserInfo;
