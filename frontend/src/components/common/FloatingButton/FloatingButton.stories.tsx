import { ComponentStory } from '@storybook/react';

import FloatingButton from '@/components/common/FloatingButton/FloatingButton';

import Writing from '@/assets/writing.svg';

export default {
  component: FloatingButton,
  title: 'Components/Common/FloatingButton',
};

const Template: ComponentStory<typeof FloatingButton> = (args) => (
  <FloatingButton {...args} />
);

export const Default = () => (
  <Template
    label={'리뷰 작성하기'}
    clickHandler={() => {
      alert('클릭됨');
    }}
  >
    <Writing />
  </Template>
);
