import SectionHeader from '@/components/common/SectionHeader/SectionHeader';

export default {
  component: SectionHeader,
  title: 'Components/SectionHeader',
};

const Template = () => (
  <SectionHeader>
    <h1>섹션 제목입니다.</h1>
  </SectionHeader>
);

export const Default = () => <Template />;
