import { useState } from 'react';

import ProductListSection from '../../components/ProductListSection/ProductListSection';
import Select from '../../components/common/Select/Select';
import useGetMany from '../../hooks/useGetMany';

const options = [
  { value: 'default', text: '기본 순' },
  { value: 'rating', text: '평점 높은 순' },
  { value: 'reviewCount', text: '리뷰 많은 순' },
];

type Product = {
  id: number;
  name: string;
  imageUrl: string;
  rating: number;
};

function Products() {
  const [sort, setSort] = useState(options[1].value);
  const [keyboards, getNextPage] = useGetMany<Product>({
    url: '/api/v1/keyboards',
    size: 15,
    sort,
  });

  return (
    <ProductListSection
      title={'인기 있는 상품'}
      data={!!keyboards && keyboards}
      getNextPage={getNextPage}
      addOn={<Select value={sort} setValue={setSort} options={options} />}
    />
  );
}

export default Products;
