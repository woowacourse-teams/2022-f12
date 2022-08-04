import { useState } from 'react';
import { Player } from '@lottiefiles/react-lottie-player';
import ROUTES from '@/constants/routes';

import * as S from '@/components/ProfileCard/ProfileCard.style';
import GithubIcon from '@/assets/github.svg';
import Chip from '@/components/common/Chip/Chip';

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
};

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
  );
  const monitor = profileProducts.find(
    (product) => product.category === 'monitor'
  );
  const stand = profileProducts.find((product) => product.category === 'stand');
  const mouse = profileProducts.find((product) => product.category === 'mouse');

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
            <a
              href={`https://github.com/${gitHubId}`}
              target="_blank"
              rel="noopener noreferrer"
            >
              <GithubIcon />
            </a>
          </S.UserNameWrapper>
          <S.UserCareer>
            <Chip paddingTopBottom={0.2} paddingLeftRight={0.4} fontSize={0.7}>
              {chipMapper[careerLevel]}
            </Chip>
            <Chip paddingTopBottom={0.2} paddingLeftRight={0.4} fontSize={0.7}>
              {chipMapper[jobType]}
            </Chip>
          </S.UserCareer>
        </S.UserInfoWrapper>
        <S.InventoryWrapper>
          <S.LeftButton onClick={handleLeftButtonClick}>{`<`}</S.LeftButton>
          <S.InventoryListWrapper>
            <S.InventoryList positionX={positionX}>
              {[keyboard, mouse, monitor, stand].map((item, index) => {
                return (
                  <S.InventoryItem key={id}>
                    <S.ProductImageWrapper>
                      {item ? (
                        <S.ProductImage src={item?.imageUrl} />
                      ) : (
                        <Player
                          autoplay
                          loop
                          src="https://assets1.lottiefiles.com/private_files/lf30_oqpbtola.json"
                          style={{ height: '80px', width: '80px' }}
                        />
                      )}
                      <S.ProductTitle>
                        {item?.name ||
                          `대표 장비로 등록된 ${
                            index === 0
                              ? '키보드'
                              : index === 1
                              ? '마우스'
                              : index === 2
                              ? '모니터'
                              : '스탠드'
                          }가 없습니다.`}
                      </S.ProductTitle>
                    </S.ProductImageWrapper>
                  </S.InventoryItem>
                );
              })}
            </S.InventoryList>
          </S.InventoryListWrapper>
          <S.RightButton onClick={handleRightButtonClick}>{`>`}</S.RightButton>
        </S.InventoryWrapper>
        <a href={`${ROUTES.PROFILE}/${id}`}>
          <S.ProfileViewButton>프로필 보기</S.ProfileViewButton>
        </a>
      </S.RightSection>
    </S.Container>
  );
}

export default ProfileCard;
