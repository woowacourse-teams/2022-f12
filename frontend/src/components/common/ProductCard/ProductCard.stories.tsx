import ProductCard from './ProductCard';
import { ComponentStory } from '@storybook/react';
import sampleKeyboard from '../../../mocks/sample_keyboard.jpg';

export default {
  component: ProductCard,
  title: 'Components/ProductCard',
};

const Template: ComponentStory<typeof ProductCard> = (args) => (
  <ProductCard {...args} />
);

const defaultArgs = {
  productImage: sampleKeyboard,
  name: '예쁜 키보드',
  rating: 5,
};

export const Default = () => <Template {...defaultArgs} />;
