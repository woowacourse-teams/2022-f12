import ReviewCard from './ReviewCard';
import { ComponentStory } from '@storybook/react';

export default {
  component: ReviewCard,
  title: 'ReviewCard',
};

const Template: ComponentStory<typeof ReviewCard> = () => <ReviewCard />;

export const Defaults: ComponentStory<typeof ReviewCard> = Template.bind({});
Defaults.args = {};
