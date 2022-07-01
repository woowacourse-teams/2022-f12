import Rating from './Rating';
import { ComponentStory } from '@storybook/react';

export default {
  component: Rating,
  title: 'Rating',
};

const Template: ComponentStory<typeof Rating> = (args) => <Rating {...args} />;

export const Defaults: ComponentStory<typeof Rating> = Template.bind({});
Defaults.args = { rating: 5 };
