import SectionHeader from './SectionHeader';
import { ComponentStory } from '@storybook/react';

export default {
  component: SectionHeader,
  title: 'SectionHeader',
};

const Template: ComponentStory<typeof SectionHeader> = () => (
  <SectionHeader>
    <h1>섹션 제목입니다.</h1>
  </SectionHeader>
);

export const Defaults: ComponentStory<typeof SectionHeader> = Template.bind({});
Defaults.args = {};
