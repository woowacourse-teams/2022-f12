import { ComponentStory } from '@storybook/react';

import ProductListSection from '@/components/Product/ProductListSection/ProductListSection';

import { products } from '@/mocks/data/products';

export default {
  component: ProductListSection,
  title: 'Section/ProductListSection',
};

const Template: ComponentStory<typeof ProductListSection> = (args) => (
  <ProductListSection {...args} />
);

export const Default = () => (
  <Template title="제품 목록" data={products} isLoading={false} isError={false} />
);
