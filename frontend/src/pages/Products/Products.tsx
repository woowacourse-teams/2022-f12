import { useState } from 'react';

import ProductListSection from '@/components/ProductListSection/ProductListSection';
import Select from '@/components/common/Select/Select';
import useProducts from '@/hooks/useProducts';
import { useSearchParams } from 'react-router-dom';
import { CATEGORY } from '@/components/common/CategoryNav/CategoryNav';

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

type CATEGORY = typeof CATEGORY;

function Products() {
  const [sort, setSort] = useState<Sort>(DefaultSort.value);
  const [searchParams] = useSearchParams();
  const category = searchParams.get('category');
  const { products, getNextPage, isLoading, isReady, isError } = useProducts({
    size: '12',
    sort,
    category,
  });

  const title =
    category in CATEGORY ? CATEGORY[category as keyof CATEGORY] : '모든 상품';

  return (
    <ProductListSection
      title={title}
      data={!!products && products}
      getNextPage={getNextPage}
      addOn={<Select value={sort} setValue={setSort} options={options} />}
      isLoading={isLoading}
      isReady={isReady}
      isError={isError}
    />
  );
}

export default Products;
