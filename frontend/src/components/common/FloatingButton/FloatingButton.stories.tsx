import FloatingButton from './FloatingButton';
import { ComponentStory } from '@storybook/react';
import Plus from '@/components/common/FloatingButton/plus.svg';

export default {
  component: FloatingButton,
  title: 'Components/FloatingButton',
};

const Template: ComponentStory<typeof FloatingButton> = (args) => (
  <FloatingButton {...args} />
);

export const Default = () => (
  <Template
    clickHandler={() => {
      alert('클릭됨');
    }}
  >
    <Plus />
  </Template>
);
