import ProductSelect from '@/components/common/ProductSelect/ProductSelect';
import { products } from '@/mocks/data';
import { useState } from 'react';

export default {
  component: ProductSelect,
  title: 'Components/ProductSelect',
};

const sampleProducts = products.slice(0, 6);

const Template = () => {
  const [value, setValue] = useState(sampleProducts[0].id);

  const handleSelectChange = (id: Product['id']) => {
    setValue(id);
  };

  return (
    <ProductSelect
      options={sampleProducts}
      value={value}
      setValue={handleSelectChange}
    />
  );
};

export const Default = () => <Template />;
