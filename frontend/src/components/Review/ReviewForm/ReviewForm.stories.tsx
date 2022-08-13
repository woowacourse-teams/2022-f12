import { ComponentStory } from '@storybook/react';

import ReviewForm from '@/components/common/ReviewForm/ReviewForm';

export default {
  component: ReviewForm,
  title: 'Components/ReviewForm',
};

const Template: ComponentStory<typeof ReviewForm> = (args) => <ReviewForm {...args} />;

const handleSubmit = async () => {
  await new Promise(() => {
    alert('제출이 완료 되었습니다.');
  });
};

export const Default = () => <Template handleSubmit={handleSubmit} />;
