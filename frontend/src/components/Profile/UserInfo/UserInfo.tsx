import { useContext, useState } from 'react';
import { useParams } from 'react-router-dom';

import * as S from '@/components/Profile/UserInfo/UserInfo.style';

import { UserDataContext } from '@/contexts/LoginContextProvider';

import useAuth from '@/hooks/useAuth';
import useFollowing from '@/hooks/useFollowing';

import { GITHUB_IMAGE_SIZE_SEARCH_PARAM } from '@/constants/link';
import { CAREER_LEVELS, JOB_TYPES } from '@/constants/profile';

type Props = {
  userData: Member;
  isOwnProfile: boolean;
};

function UserInfo({ userData, isOwnProfile }: Props) {
  const {
    imageUrl,
    gitHubId,
    jobType,
    careerLevel,
    followerCount: initialFollowerCount,
    following,
  } = userData;
  const { isLoggedIn } = useAuth();

  const loginUserData = useContext(UserDataContext);
  const loginUserGitHubId = loginUserData && loginUserData.member.gitHubId;
  const otherProfilePage = !isOwnProfile && loginUserGitHubId !== gitHubId;

  const { memberId } = useParams();
  const [followed, setFollowed] = useState(following);
  const [followerCount, setFollowerCount] = useState(initialFollowerCount);
  const { followUser, unfollowUser } = useFollowing(Number(memberId));

  const toggleFollow = async () => {
    try {
      if (followed) {
        await unfollowUser();
        setFollowed((prev) => !prev);
        setFollowerCount((prev) => prev - 1);
        return;
      }

      await followUser();
      setFollowed((prev) => !prev);
      setFollowerCount((prev) => prev + 1);
    } catch {
      return;
    }
  };

  return (
    <>
      <S.Container>
        <S.ImageWrapper>
          <S.ProfileImage
            src={`${imageUrl}${GITHUB_IMAGE_SIZE_SEARCH_PARAM.medium}`}
            alt=""
          />
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
      {isLoggedIn && otherProfilePage && (
        <S.FollowButton followed={followed} onClick={toggleFollow}>
          {followed ? '팔로잉' : '팔로우'}
        </S.FollowButton>
      )}
    </>
  );
}

export default UserInfo;
