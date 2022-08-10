import { useState } from 'react';
import { Player } from '@lottiefiles/react-lottie-player';
import ROUTES from '@/constants/routes';
import { Link } from 'react-router-dom';

import * as S from '@/components/ProfileCard/ProfileCard.style';
import GithubIcon from '@/assets/github.svg';
import Chip from '@/components/common/Chip/Chip';
import PrevSign from '@/assets/prevSign.svg';
import NextSign from '@/assets/nextSign.svg';

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
} as const;

function ProfileCard({
  id,
  gitHubId,
  imageUrl,
  careerLevel,
  jobType,
  profileProducts,
}: Props) {
  const [positionX, setPositionX] = useState(0);

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

  const keyboard = profileProducts.find(
    (product) => product.category === 'keyboard'
  ) || { id: null, imageUrl: null, name: '키보드' };
  const mouse = profileProducts.find(
    (product) => product.category === 'mouse'
  ) || { id: null, imageUrl: null, name: '마우스' };
  const monitor = profileProducts.find(
    (product) => product.category === 'monitor'
  ) || { id: null, imageUrl: null, name: '모니터' };
  const stand = profileProducts.find(
    (product) => product.category === 'stand'
  ) || { id: null, imageUrl: null, name: '거치대' };
  const representativeEquipments = [keyboard, mouse, monitor, stand].sort(
    (prevEquipment, nextEquipment) => {
      return nextEquipment?.name.length - prevEquipment?.name.length;
    }
  );

  console.log(representativeEquipments);

  return (
    <S.Container>
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
              href={`https://github.com/${gitHubId}`}
              target="_blank"
              rel="noopener noreferrer"
            >
              <GithubIcon />
            </S.LinkWrapper>
          </S.UserNameWrapper>
          <S.UserCareer>
            {[careerLevel, jobType].map((userInfo, index) => {
              return (
                <Chip
                  key={index}
                  paddingTopBottom={0.2}
                  paddingLeftRight={0.4}
                  fontSize={0.7}
                >
                  {chipMapper[userInfo]}
                </Chip>
              );
            })}
          </S.UserCareer>
        </S.UserInfoWrapper>
        <S.InventoryWrapper>
          <S.LeftButton onClick={handleLeftButtonClick}>
            <PrevSign />
          </S.LeftButton>
          <S.InventoryListWrapper>
            <S.InventoryList positionX={positionX}>
              {representativeEquipments.map((equipment, index) => {
                console.log(equipment);
                return (
                  <S.InventoryItem key={gitHubId + String(index)}>
                    <S.ProductImageWrapper>
                      {equipment?.id ? (
                        <Link
                          to={`${ROUTES.PRODUCT}/${equipment?.id as string}`}
                        >
                          <S.ProductImage src={equipment?.imageUrl as string} />
                        </Link>
                      ) : (
                        <Player
                          autoplay
                          loop
                          src="https://assets1.lottiefiles.com/private_files/lf30_oqpbtola.json"
                          style={{ height: '80px', width: '80px' }}
                        />
                      )}
                      <S.ProductTitle>
                        {equipment.id
                          ? equipment.name
                          : `대표 장비로 등록된 ${equipment.name}가 없습니다.`}
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
