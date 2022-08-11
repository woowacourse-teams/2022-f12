import { ComponentStory } from '@storybook/react';

import BarGraph from '@/components/common/BarGraph/BarGraph';

export default {
  component: BarGraph,
  title: 'Components/BarGraph',
};

const Template: ComponentStory<typeof BarGraph> = (args) => <BarGraph {...args} />;

const statistics = {
  careerLevel: {
    midlevel: 0.2,
    senior: 0.3,
    none: 0.1,
    junior: 0.4,
  },
  jobType: {
    frontend: 0.45,
    backend: 0.25,
    mobile: 0.2,
    etc: 0.1,
  },
};

export const Default = () => <Template statistics={statistics} />;
