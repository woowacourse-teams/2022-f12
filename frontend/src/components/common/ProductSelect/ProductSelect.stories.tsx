import ProductSelect from '@/components/common/ProductSelect/ProductSelect';
import { products } from '@/mocks/data';
import { useState } from 'react';

export default {
  component: ProductSelect,
  title: 'Components/ProductSelect',
};

const sampleProducts = products.slice(0, 6);

const Template = () => {
  return (
    <ProductSelect
      submitHandler={() => {
        console.log('hi');
      }}
      updateProfileProduct={() => {}}
      inventoryList={{}}
    />
  );
};

export const Default = () => <Template />;
