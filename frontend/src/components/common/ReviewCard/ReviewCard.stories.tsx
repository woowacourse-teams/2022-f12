import ReviewCard from './ReviewCard';

export default {
  component: ReviewCard,
  title: 'ReviewCard',
};

const Template = (args) => <ReviewCard {...args} />;

export const Defaults = Template.bind({});
Defaults.args = {};
