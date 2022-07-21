import { useState } from 'react';

import ProductListSection from '@/components/ProductListSection/ProductListSection';
import Select from '@/components/common/Select/Select';
import useProducts from '@/hooks/useProducts';

type Option = { value: string; text: string };

type Sort = 'rating,desc' | 'reviewCount,desc';
type SortText = '평점 높은 순' | '리뷰 많은 순';

interface ProductSortOption extends Option {
  value: Sort;
  text: SortText;
}

const options: ProductSortOption[] = [
  { value: 'rating,desc', text: '평점 높은 순' },
  { value: 'reviewCount,desc', text: '리뷰 많은 순' },
];

const DefaultSort = options[1];

function Products() {
  const [sort, setSort] = useState<Sort>(DefaultSort.value);
  const [keyboards, getNextPage] = useProducts({
    size: 12,
    sort,
  });

  return (
    <ProductListSection
      title={'모든 상품 목록'}
      data={!!keyboards && keyboards}
      getNextPage={getNextPage}
      addOn={<Select value={sort} setValue={setSort} options={options} />}
    />
  );
}

export default Products;
