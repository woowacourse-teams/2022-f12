import { useState } from 'react';

import ProductListSection from '../../components/ProductListSection/ProductListSection';
import Select from '../../components/common/Select/Select';
import { products } from './mockData';

const options = [
  { value: 'default', text: '기본 순' },
  { value: 'rating', text: '평점 높은 순' },
  { value: 'reviewCount', text: '리뷰 많은 순' },
];

function Products() {
  const [sorting, setSorting] = useState(options[1].value);

  return (
    <ProductListSection
      title={'인기 있는 상품'}
      data={products}
      addOn={<Select value={sorting} setValue={setSorting} options={options} />}
    />
  );
}

export default Products;
