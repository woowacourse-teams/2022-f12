import { ComponentStory } from '@storybook/react';

import Chip from '@/components/common/Chip/Chip';

export default {
  component: Chip,
  title: 'Components/Chip',
};

const Template: ComponentStory<typeof Chip> = (args) => <Chip {...args} />;

export const Default = () => (
  <Template paddingTopBottom={0.4} paddingLeftRight={1} fontSize={1}>
    프론트엔드
  </Template>
);
