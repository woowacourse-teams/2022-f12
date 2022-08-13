import { ComponentStory } from '@storybook/react';

import ProductCard from '@/components/Product/ProductCard/ProductCard';

import { products } from '@/mocks/data';

export default {
  component: ProductCard,
  title: 'Components/ProductCard',
};

const Template: ComponentStory<typeof ProductCard> = (args) => <ProductCard {...args} />;

const defaultArgs = {
  ...products[0],
};

export const Default = () => <Template {...defaultArgs} />;
