import { useParams } from 'react-router-dom';

import * as S from '@/components/Profile/UserInfo/UserInfo.style';

import { CAREER_LEVELS, JOB_TYPES } from '@/constants/profile';

type Props = {
  userData: Member;
};

function UserInfo({ userData }: Props) {
  const { imageUrl, gitHubId, jobType, careerLevel } = userData;
  const { memberId } = useParams();

  const isOwnProfile = !memberId;

  return (
    <>
      <S.Container>
        <S.ImageWrapper>
          <S.ProfileImage src={imageUrl} alt="" />
        </S.ImageWrapper>
        <S.InfoWrapper>
          {`${CAREER_LEVELS[careerLevel]}, `}
          {`${JOB_TYPES[jobType]} 개발자`}
          <S.UserNameWrapper>
            <S.GitHubId
              href={`https://www.github.com/${gitHubId}`}
              target="_blank"
              rel="noopener noreferrer"
            >
              {`@${gitHubId}`}
            </S.GitHubId>
            {`의 데스크 셋업`}
          </S.UserNameWrapper>
          <S.FollowerCount>0명이 팔로우함</S.FollowerCount>
        </S.InfoWrapper>
      </S.Container>
      {!isOwnProfile && <S.FollowButton>팔로우</S.FollowButton>}
    </>
  );
}

export default UserInfo;
