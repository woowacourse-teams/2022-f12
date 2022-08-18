import SectionHeader from '@/components/common/SectionHeader/SectionHeader';

export default {
  component: SectionHeader,
  title: 'Components/Common/SectionHeader',
};

const Template = () => <SectionHeader title="섹션 제목입니다" />;

export const Default = () => <Template />;
