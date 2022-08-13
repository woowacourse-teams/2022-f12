import Chip from '@/components/common/Chip/Chip';

import * as S from '@/components/Profile/UserInfo/UserInfo.style';

import GithubIcon from '@/assets/github.svg';

type Props = {
  userData: Member;
};

const chipMapper = {
  frontend: '프론트엔드',
  backend: '백엔드',
  mobile: '모바일',
  etc: '기타',
  none: '경력 없음',
  junior: '0-2년차',
  midlevel: '3-5년차',
  senior: '6년차 이상',
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
          href={`https://github.com/${gitHubId}`}
          target="_blank"
          rel="noopener noreferrer"
        >
          <GithubIcon />
        </S.GithubLink>
      </S.UserNameWrapper>
      {
        <S.ChipWrapper>
          <Chip size="l">{chipMapper[jobType]}</Chip>
          <Chip size="l">{chipMapper[careerLevel]}</Chip>
        </S.ChipWrapper>
      }
    </S.Container>
  );
}

export default UserInfo;
