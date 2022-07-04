import Header from './Header';
import { ComponentStory } from '@storybook/react';

export default {
  component: Header,
  title: 'Components/Header',
};

const Template: ComponentStory<typeof Header> = () => <Header />;

export const Defaults: ComponentStory<typeof Header> = Template.bind({});
Defaults.args = {};
