import BarGraph from '@/components/common/BarGraph/BarGraph';
import { ComponentStory } from '@storybook/react';

export default {
  component: BarGraph,
  title: 'Components/BarGraph',
};

const Template: ComponentStory<typeof BarGraph> = () => <BarGraph />;

export const Default = () => <Template />;
