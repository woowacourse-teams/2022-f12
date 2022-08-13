import { ComponentStory } from '@storybook/react';

import WishButton from '@/components/common/WishButton/WishButton';

export default {
  component: WishButton,
  title: 'Components/Common/WishButton',
};

const Template: ComponentStory<typeof WishButton> = (args) => <WishButton {...args} />;

export const added = () => <Template added={true} />;
export const unAdded = () => <Template added={false} />;
