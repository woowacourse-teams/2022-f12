import SearchFilter from '@/components/SearchFilter/SearchFilter';
import { ComponentStory } from '@storybook/react';

export default {
  component: SearchFilter,
  title: 'Components/SearchFilter',
};

const Template: ComponentStory<typeof SearchFilter> = () => <SearchFilter />;

export const Default = () => <Template />;
