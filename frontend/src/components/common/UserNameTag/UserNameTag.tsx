import * as S from '@/components/common/UserNameTag/UserNameTag.style';

type Props = {
  imageUrl: string;
  username: string;
};

function UserNameTag({ imageUrl, username = '' }: Props) {
  return (
    <S.Container>
      <S.Avatar src={imageUrl} alt="" />
      <S.Username>{username}</S.Username>
    </S.Container>
  );
}

export default UserNameTag;
