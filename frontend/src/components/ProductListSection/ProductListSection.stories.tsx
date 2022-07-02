import ProductListSection from './ProductListSection';
import { ComponentStory } from '@storybook/react';
import sampleKeyboard from './sample_keyboard.jpg';

export default {
  component: ProductListSection,
  title: 'ProductListSection',
};

const mockData = [
  { id: 1, name: '예쁜 키보드', productImage: sampleKeyboard, rating: 5 },
  { id: 2, name: '예쁜 키보드', productImage: sampleKeyboard, rating: 5 },
  { id: 3, name: '예쁜 키보드', productImage: sampleKeyboard, rating: 5 },
  { id: 4, name: '예쁜 키보드', productImage: sampleKeyboard, rating: 5 },
  { id: 5, name: '예쁜 키보드', productImage: sampleKeyboard, rating: 5 },
];

const Template: ComponentStory<typeof ProductListSection> = () => (
  <ProductListSection data={mockData} />
);

export const Defaults: ComponentStory<typeof ProductListSection> =
  Template.bind({});
Defaults.args = {};
