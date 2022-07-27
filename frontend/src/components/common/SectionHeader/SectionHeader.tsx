import * as S from '@/components/common/SectionHeader/SectionHeader.style';

type Prop = {
  children: React.ReactNode;
};

function SectionHeader({ children }: Prop) {
  return <S.Container>{children}</S.Container>;
}

export default SectionHeader;
