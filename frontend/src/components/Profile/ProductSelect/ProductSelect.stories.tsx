import ProductSelect from '@/components/Profile/ProductSelect/ProductSelect';

import { products } from '@/mocks/data';

export default {
  component: ProductSelect,
  title: 'Components/Profile/ProductSelect',
};

const sampleProducts = products.slice(0, 6);

const Template = (args) => {
  return <ProductSelect inventoryList={sampleProducts} {...args} />;
};

export const Default = () => <Template />;
