import * as S from '@/components/common/SectionHeader/SectionHeader.style';

type Props = {
  title: string;
  children?: React.ReactNode;
};

function SectionHeader({ title, children }: Props) {
  return (
    <S.Container>
      <S.Title>{title}</S.Title>
      {children}
    </S.Container>
  );
}

export default SectionHeader;
