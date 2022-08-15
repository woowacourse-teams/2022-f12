import { ComponentStory } from '@storybook/react';

import Rating from '@/components/common/Rating/Rating';

export default {
  component: Rating,
  title: 'Components/Common/Rating',
};

const Template: ComponentStory<typeof Rating> = (args) => <Rating {...args} />;

export const Default = () => <Template rating={5} />;
