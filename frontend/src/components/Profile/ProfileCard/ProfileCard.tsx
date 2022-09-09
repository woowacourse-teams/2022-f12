import { useState } from 'react';
import { Link } from 'react-router-dom';

import Chip from '@/components/common/Chip/Chip';

import * as S from '@/components/Profile/ProfileCard/ProfileCard.style';

import useAuth from '@/hooks/useAuth';
import useFollowing from '@/hooks/useFollowing';

import TITLE from '@/constants/header';
import { GITHUB_URL } from '@/constants/link';
import { CAREER_LEVELS, JOB_TYPES } from '@/constants/profile';
import ROUTES from '@/constants/routes';

import Empty from '@/assets/empty.svg';
import GithubIcon from '@/assets/github.svg';
import NextSign from '@/assets/nextSign.svg';
import PrevSign from '@/assets/prevSign.svg';

const DISTANCE_DIFFERENCE = 116;

type ProfileProduct = {
  id: number;
  name: string;
  imageUrl: string;
  reviewCount: number;
  rating: number;
  category: string;
};

type Props = {
  id: number;
  gitHubId: string;
  imageUrl: string;
  careerLevel: string;
  jobType: string;
  profileProducts: ProfileProduct[];
  followerCount: number;
  following: boolean;
  index?: number;
};

function ProfileCard({
  id,
  gitHubId,
  imageUrl,
  careerLevel,
  jobType,
  profileProducts,
  followerCount: initialFollowerCount,
  following,
  index = 0,
}: Props) {
  const [positionX, setPositionX] = useState(0);
  const [followed, setFollowed] = useState(following);
  const [followerCount, setFollowerCount] = useState(initialFollowerCount);
  const { isLoggedIn } = useAuth();

  const { followUser, unfollowUser } = useFollowing(id);

  const handleLeftButtonClick = () => {
    if (positionX === 0) {
      setPositionX(DISTANCE_DIFFERENCE * -2);
      return;
    }
    setPositionX(positionX + DISTANCE_DIFFERENCE * 1);
  };

  const handleRightButtonClick = () => {
    if (positionX <= DISTANCE_DIFFERENCE * -2) {
      setPositionX(0);
      return;
    }
    setPositionX(positionX + DISTANCE_DIFFERENCE * -1);
  };

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

  const keyboard = profileProducts.find((product) => product.category === 'keyboard') || {
    id: null,
    imageUrl: null,
    name: '키보드',
  };
  const mouse = profileProducts.find((product) => product.category === 'mouse') || {
    id: null,
    imageUrl: null,
    name: '마우스',
  };
  const monitor = profileProducts.find((product) => product.category === 'monitor') || {
    id: null,
    imageUrl: null,
    name: '모니터',
  };
  const stand = profileProducts.find((product) => product.category === 'stand') || {
    id: null,
    imageUrl: null,
    name: '거치대',
  };
  const representativeEquipments = [keyboard, mouse, monitor, stand].sort(
    (prevEquipment, nextEquipment) => {
      return nextEquipment?.name.length - prevEquipment?.name.length;
    }
  );

  return (
    <S.Container index={index}>
      <S.LeftSection>
        <S.ProfileImageWrapper>
          <S.ProfileImage src={imageUrl} />
        </S.ProfileImageWrapper>
      </S.LeftSection>
      <S.RightSection>
        <S.UserInfoWrapper>
          <S.UserNameWrapper>
            <S.UserName>{gitHubId}</S.UserName>
            <S.LinkWrapper
              href={`${GITHUB_URL}${gitHubId}`}
              target="_blank"
              rel="noopener noreferrer"
            >
              <GithubIcon />
            </S.LinkWrapper>
          </S.UserNameWrapper>
          <S.FollowerCountWrapper>{followerCount}명이 팔로우함</S.FollowerCountWrapper>
          <S.UserCareer>
            <Chip size="s">{JOB_TYPES[jobType]}</Chip>
            <Chip size="s">{CAREER_LEVELS[careerLevel]}</Chip>
          </S.UserCareer>
        </S.UserInfoWrapper>
        {isLoggedIn && (
          <S.FollowingButtonWrapper>
            <S.FollowingButton followed={followed} onClick={toggleFollow}>
              {followed ? '팔로잉' : '팔로우'}
            </S.FollowingButton>
          </S.FollowingButtonWrapper>
        )}
        <S.InventoryWrapper>
          <S.LeftButton onClick={handleLeftButtonClick}>
            <PrevSign />
          </S.LeftButton>
          <S.InventoryListWrapper>
            <S.InventoryList positionX={positionX}>
              {representativeEquipments.map((equipment, index) => {
                return (
                  <S.InventoryItem key={index}>
                    <S.ProductImageWrapper>
                      {equipment.id ? (
                        <Link to={`${ROUTES.PRODUCT}/${equipment.id as string}`}>
                          <S.ProductImage src={equipment.imageUrl as string} />
                        </Link>
                      ) : (
                        <Empty />
                      )}
                      <S.ProductTitle>
                        {equipment.id ? (
                          <Link to={`${ROUTES.PRODUCT}/${equipment.id as string}`}>
                            {equipment.name}
                          </Link>
                        ) : (
                          `${TITLE.DESK_SETUP}에 추가한 ${equipment.name}가 없어요`
                        )}
                      </S.ProductTitle>
                    </S.ProductImageWrapper>
                  </S.InventoryItem>
                );
              })}
            </S.InventoryList>
          </S.InventoryListWrapper>
          <S.RightButton onClick={handleRightButtonClick}>
            <NextSign />
          </S.RightButton>
        </S.InventoryWrapper>
        <S.LinkWrapper href={`${ROUTES.PROFILE}/${id}`}>
          <S.ProfileViewButton>프로필 보기</S.ProfileViewButton>
        </S.LinkWrapper>
      </S.RightSection>
    </S.Container>
  );
}

export default ProfileCard;
