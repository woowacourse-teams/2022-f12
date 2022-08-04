import ProductCard from '@/components/common/ProductCard/ProductCard';
import { products } from '@/mocks/data';
import { ComponentStory } from '@storybook/react';

export default {
  component: ProductCard,
  title: 'Components/ProductCard',
};

const Template: ComponentStory<typeof ProductCard> = (args) => (
  <ProductCard {...args} />
);

const defaultArgs = {
  ...products[0],
};

export const Default = () => <Template {...defaultArgs} />;
