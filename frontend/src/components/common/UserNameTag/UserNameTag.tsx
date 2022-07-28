import * as S from '@/components/common/UserNameTag/UserNameTag.style';

type Props = {
  profileImage: string;
  username: string;
};

function UserNameTag({ profileImage, username = '' }: Props) {
  return (
    <S.Container>
      <S.Avatar src={profileImage} alt={``} />
      <S.Username>{username}</S.Username>
    </S.Container>
  );
}

export default UserNameTag;
