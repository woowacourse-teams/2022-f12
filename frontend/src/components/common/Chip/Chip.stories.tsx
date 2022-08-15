import { ComponentStory } from '@storybook/react';

import Chip from '@/components/common/Chip/Chip';

export default {
  component: Chip,
  title: 'Components/Common/Chip',
};

const Template: ComponentStory<typeof Chip> = (args) => <Chip {...args} />;

export const Default = () => <Template size={'s'}>프론트엔드</Template>;
