import Chip from '@/components/common/Chip/Chip';
import * as S from '@/components/common/UserInfo/UserInfo.styled';

type Props = {
  profileImageUrl: string;
  username: string;
  jobType?: string;
  career?: string;
};
function UserInfo({ profileImageUrl, username, jobType, career }: Props) {
  return (
    <S.Container>
      <S.ImageWrapper>
        <S.ProfileImage src={profileImageUrl} alt="" />
      </S.ImageWrapper>
      <S.Username>{username}</S.Username>
      {/* <S.ChipWrapper>
        <Chip>{jobType}</Chip>
        <Chip>{career}</Chip>
      </S.ChipWrapper> */}
    </S.Container>
  );
}

export default UserInfo;
