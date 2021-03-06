import ReviewCard from './ReviewCard';
import { ComponentStory } from '@storybook/react';

import { reviewsWithProduct } from '../../../mocks/data';

export default {
  component: ReviewCard,
  title: 'Components/ReviewCard',
};

const Template: ComponentStory<typeof ReviewCard> = (args) => (
  <ReviewCard {...args} />
);

const defaultArgs = reviewsWithProduct[0];

export const Default = () => <Template {...defaultArgs} />;
