import { useContext } from 'react';

import * as S from '@/components/Profile/UserInfo/UserInfo.style';

import { UserDataContext } from '@/contexts/LoginContextProvider';

import useAuth from '@/hooks/useAuth';

import { CAREER_LEVELS, JOB_TYPES } from '@/constants/profile';

type Props = {
  userData: Member;
  isOwnProfile: boolean;
};

function UserInfo({ userData, isOwnProfile }: Props) {
  const { imageUrl, gitHubId, jobType, careerLevel, followerCount } = userData;
  const { isLoggedIn } = useAuth();
  const loginUserData = useContext(UserDataContext);
  const loginUserGitHubId = loginUserData && loginUserData.member.gitHubId;
  const otherProfilePage = !isOwnProfile && loginUserGitHubId !== gitHubId;

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
          <S.FollowerCount>{followerCount}명이 팔로우함</S.FollowerCount>
        </S.InfoWrapper>
      </S.Container>
      {isLoggedIn && otherProfilePage && <S.FollowButton>팔로우</S.FollowButton>}
    </>
  );
}

export default UserInfo;
