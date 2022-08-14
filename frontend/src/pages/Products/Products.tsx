import { useMemo } from 'react';

import * as S from '@/pages/Products/Products.style';

import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import { CATEGORY } from '@/components/common/CategoryNav/CategoryNav';
import Loading from '@/components/common/Loading/Loading';
import SearchBar from '@/components/common/SearchBar/SearchBar';
import SearchFilter from '@/components/common/SearchFilter/SearchFilter';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';
import Select from '@/components/common/Select/Select';

import ProductListSection from '@/components/Product/ProductListSection/ProductListSection';

import useSearch from '@/hooks/useSearch';
import useUrlSyncState from '@/hooks/useUrlSyncState';

import { ENDPOINTS } from '@/constants/api';

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

const categories = {
  keyboard: '키보드',
  mouse: '마우스',
  monitor: '모니터',
  stand: '거치대',
  software: '소프트웨어',
} as const;

function Products() {
  const [keyword, setKeyword] = useUrlSyncState('keyword');
  const [category, setCategory] = useUrlSyncState('category');
  const [sort, setSort] = useUrlSyncState('sort', DefaultSort.value);

  const {
    result: products,
    getNextPage,
    isLoading,
    isReady,
    isError,
  } = useSearch<Product>({
    url: ENDPOINTS.PRODUCTS,
    size: '12',
    query: keyword,
    filter: {
      category,
      sort,
    },
  });

  const title = useMemo(
    () => (category in CATEGORY ? CATEGORY[category as keyof CATEGORY] : '모든 제품'),
    [category]
  );

  return (
    <>
      <S.SearchBarWrapper>
        <SearchBar searchInput={keyword} setSearchInput={setKeyword} />
        <SearchFilter
          title={'카테고리'}
          value={category}
          setValue={setCategory}
          options={categories}
        />
      </S.SearchBarWrapper>
      <AsyncWrapper fallback={<Loading />} isReady={isReady} isError={isError}>
        <SectionHeader title={title}>
          <Select value={sort} setValue={setSort} options={options} />
        </SectionHeader>
        <ProductListSection
          data={products}
          getNextPage={getNextPage}
          isLoading={isLoading}
          isError={isError}
        />
      </AsyncWrapper>
    </>
  );
}

export default Products;
