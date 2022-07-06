import { useState } from 'react';

import ProductListSection from '../../components/ProductListSection/ProductListSection';
import Select from '../../components/common/Select/Select';
import useProducts from '../../hooks/useProducts';

type Option = { value: string; text: string };

type Sort = 'default' | 'rating' | 'reviewCount';
type SortText = '기본 순' | '평점 높은 순' | '리뷰 많은 순';

interface ProductSortOption extends Option {
  value: Sort;
  text: SortText;
}

const options: ProductSortOption[] = [
  { value: 'default', text: '기본 순' },
  { value: 'rating', text: '평점 높은 순' },
  { value: 'reviewCount', text: '리뷰 많은 순' },
];

const DefaultSort = options[1];

function Products() {
  const [sort, setSort] = useState<Sort>(DefaultSort.value);
  const [keyboards, getNextPage] = useProducts({
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
