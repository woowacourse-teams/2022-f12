import ReviewCard from './ReviewCard';
import { ComponentStory } from '@storybook/react';

import { reviews } from '../../../mocks/data';

export default {
  component: ReviewCard,
  title: 'Components/ReviewCard',
};

const Template: ComponentStory<typeof ReviewCard> = (args) => (
  <ReviewCard {...args} />
);

const defaultArgs = reviews[0];

export const Default = () => <Template {...defaultArgs} />;
