import { ComponentStory } from '@storybook/react';

import ReviewForm from '@/components/Review/ReviewForm/ReviewForm';

export default {
  component: ReviewForm,
  title: 'Components/Review/ReviewForm',
};

const Template: ComponentStory<typeof ReviewForm> = (args) => <ReviewForm {...args} />;

const handleSubmit = async () => {
  await new Promise(() => {
    alert('제출이 완료 되었습니다.');
  });
};

export const EditReviewForm = () => (
  <Template handleSubmit={handleSubmit} isEdit={true} />
);
export const WriteReviewForm = () => (
  <Template handleSubmit={handleSubmit} isEdit={false} />
);
