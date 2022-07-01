import Rating from './Rating';

export default {
  component: Rating,
  title: 'Rating',
};

const Template = (args) => <Rating {...args} />;

export const Defaults = Template.bind({});
Defaults.args = { rating: '5.00' };
