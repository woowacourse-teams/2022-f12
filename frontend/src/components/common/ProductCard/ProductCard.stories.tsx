import ProductCard from './ProductCard';
import { ComponentStory } from '@storybook/react';
import sampleKeyboard from './sample_keyboard.jpg';

export default {
  component: ProductCard,
  title: 'ProductCard',
};

const Template: ComponentStory<typeof ProductCard> = (args) => (
  <ProductCard {...args} />
);

export const Defaults: ComponentStory<typeof ProductCard> = Template.bind({});
Defaults.args = {
  productImage: sampleKeyboard,
  name: '예쁜 키보드',
  rating: 5,
};
