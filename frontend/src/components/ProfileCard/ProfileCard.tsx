import { useState } from 'react';

import * as S from '@/components/ProfileCard/ProfileCard.style';
import sampleProfile from '@/mocks/sample_profile.jpg';
import sampleKeyboard from '@/mocks/sample_keyboard.jpg';
import GithubIcon from '@/assets/github.svg';
import Chip from '@/components/common/Chip/Chip';

function ProfileCard() {
  const [positionX, setPositionX] = useState(0);

  const handleLeftButtonClick = () => {
    if (positionX === 0) {
      setPositionX(-224);
      return;
    }
    setPositionX(positionX + 112);
  };

  const handleRightButtonClick = () => {
    if (positionX <= -224) {
      setPositionX(0);
      return;
    }
    setPositionX(positionX - 112);
  };

  console.log(positionX);

  return (
    <S.Container>
      <S.LeftSection>
        <S.ProfileImageWrapper>
          <S.ProfileImage src={sampleProfile} />
        </S.ProfileImageWrapper>
      </S.LeftSection>
      <S.RightSection>
        <S.UserInfoWrapper>
          <S.UserNameWrapper>
            <S.UserName>칙촉</S.UserName>
            <GithubIcon />
          </S.UserNameWrapper>
          <S.UserCareer>
            <Chip paddingTopBottom={0.2} paddingLeftRight={0.4} fontSize={0.7}>
              0-3년차
            </Chip>
            <Chip paddingTopBottom={0.2} paddingLeftRight={0.4} fontSize={0.7}>
              프론트엔드
            </Chip>
          </S.UserCareer>
        </S.UserInfoWrapper>
        <S.InventoryWrapper>
          <S.LeftButton onClick={handleLeftButtonClick}>{`<`}</S.LeftButton>
          <S.InventoryListWrapper>
            <S.InventoryList positionX={positionX}>
              {['키보드', '마우스', '모니터', '거치대'].map(
                (product, index) => {
                  return (
                    <S.InventoryItem key={index}>
                      <S.ProductImageWrapper>
                        <S.ProductImage src={sampleKeyboard}></S.ProductImage>
                        <S.ProductTitle>{product}</S.ProductTitle>
                      </S.ProductImageWrapper>
                    </S.InventoryItem>
                  );
                }
              )}
            </S.InventoryList>
          </S.InventoryListWrapper>
          <S.RightButton onClick={handleRightButtonClick}>{`>`}</S.RightButton>
        </S.InventoryWrapper>
        <S.ProfileViewButton>프로필 보기</S.ProfileViewButton>
      </S.RightSection>
    </S.Container>
  );
}

export default ProfileCard;
