import Header from './Header';
import { ComponentStory } from '@storybook/react';

export default {
  component: Header,
  title: 'Components/Header',
};

const Template: ComponentStory<typeof Header> = () => <Header />;

export const Default = () => <Template />;
