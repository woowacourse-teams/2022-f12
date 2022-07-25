import FloatingButton from '@/components/common/FloatingButton/FloatingButton';
import { ComponentStory } from '@storybook/react';
import Plus from '@/assets/plus.svg';
import theme from '@/style/theme';

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
    <Plus stroke={theme.colors.white} />
  </Template>
);
