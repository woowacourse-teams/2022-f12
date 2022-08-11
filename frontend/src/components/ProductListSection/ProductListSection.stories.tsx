import ProductListSection from '@/components/ProductListSection/ProductListSection';
import { ComponentStory } from '@storybook/react';
import { products } from '@/mocks/data';

export default {
  component: ProductListSection,
  title: 'Section/ProductListSection',
};

const Template: ComponentStory<typeof ProductListSection> = (args) => (
  <ProductListSection {...args} />
);

export const Default = () => (
  <Template
    title="모든 상품목록"
    data={products}
    isLoading={false}
    isError={false}
    isReady={true}
  />
);
