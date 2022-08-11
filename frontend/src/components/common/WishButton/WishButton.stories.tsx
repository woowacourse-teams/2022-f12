import { ComponentStory } from '@storybook/react';

import WishButton from '@/components/common/WishButton/WishButton';

export default {
  component: WishButton,
  title: 'Components/WishButton',
};

const Template: ComponentStory<typeof WishButton> = (args) => (
  <WishButton {...args} />
);

export const added = () => <Template added={true} />;
export const unAdded = () => <Template added={false} />;
