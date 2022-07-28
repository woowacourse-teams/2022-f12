import * as S from '@/components/ProfileCard/ProfileCard.style';
import sampleProfile from '@/mocks/sample_profile.jpg';
import sampleKeyboard from '@/mocks/sample_keyboard.jpg';
import GithubIcon from '@/assets/github.svg';
import Chip from '@/components/common/Chip/Chip';

function ProfileCard() {
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
          <S.LeftButton>{`<`}</S.LeftButton>
          <S.InventoryList>
            <S.InventoryItem>
              <S.ProductImageWrapper>
                <S.ProductImage src={sampleKeyboard}></S.ProductImage>
                <S.ProductTitle>
                  너무너무 긴 제목을 가진 마우스 마우스 마우스
                </S.ProductTitle>
              </S.ProductImageWrapper>
            </S.InventoryItem>
            <S.InventoryItem>
              <S.ProductImageWrapper>
                <S.ProductImage src={sampleKeyboard}></S.ProductImage>
                <S.ProductTitle>로지텍 MX Master 2S</S.ProductTitle>
              </S.ProductImageWrapper>
            </S.InventoryItem>
          </S.InventoryList>
          <S.RightButton>{`>`}</S.RightButton>
        </S.InventoryWrapper>
        <S.ProfileViewButton>프로필 보기</S.ProfileViewButton>
      </S.RightSection>
    </S.Container>
  );
}

export default ProfileCard;
