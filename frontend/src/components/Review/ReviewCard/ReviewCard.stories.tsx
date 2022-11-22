import { ComponentStory } from '@storybook/react';

import ReviewCard from '@/components/Review/ReviewCard/ReviewCard';

import { reviewsWithProduct } from '@/mocks/data/reviews';

export default {
  component: ReviewCard,
  title: 'Components/Review/ReviewCard',
};

const Template: ComponentStory<typeof ReviewCard> = (args) => <ReviewCard {...args} />;

const reviewData = reviewsWithProduct[0];

export const Default = () => <Template reviewData={reviewData} />;
