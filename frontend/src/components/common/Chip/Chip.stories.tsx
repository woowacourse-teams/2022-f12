import Chip from '@/components/common/Chip/Chip';
import { ComponentStory } from '@storybook/react';

export default {
  component: Chip,
  title: 'Components/Chip',
};

const Template: ComponentStory<typeof Chip> = (args) => <Chip {...args} />;

export const Default = () => <Template>프론트엔드</Template>;
