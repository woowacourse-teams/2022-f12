import Chip from '@/components/common/Chip/Chip';
import * as S from '@/components/common/UserInfo/UserInfo.styled';

type Member = {
  id: string;
  gitHubId: string;
  name: string;
  imageUrl: string;
  careerLevel: string;
  jobType: string;
};

type Props = {
  userData: Member;
};

function UserInfo({ userData }: Props) {
  const { imageUrl, gitHubId } = userData;
  return (
    <S.Container>
      <S.ImageWrapper>
        <S.ProfileImage src={imageUrl} alt="" />
      </S.ImageWrapper>
      <S.Username>{`@${gitHubId}`}</S.Username>
      {/* <S.ChipWrapper>
        <Chip>{jobType}</Chip>
        <Chip>{career}</Chip>
      </S.ChipWrapper> */}
    </S.Container>
  );
}

export default UserInfo;
