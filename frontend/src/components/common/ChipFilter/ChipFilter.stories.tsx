import ChipFilter from '@/components/common/ChipFilter/ChipFilter';
import { ComponentStory } from '@storybook/react';

export default {
  component: ChipFilter,
  title: 'Components/ChipFilter',
};

const Template: ComponentStory<typeof ChipFilter> = (args) => (
  <ChipFilter {...args} />
);

export const Clicked = () => (
  <Template fontSize={10} clicked={true}>
    경력 없음
  </Template>
);

export const unClicked = () => (
  <Template fontSize={10} clicked={false}>
    경력 없음
  </Template>
);
