import { useEffect, useMemo, useState } from 'react';

import * as S from '@/pages/Products/Products.style';

import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import Loading from '@/components/common/Loading/Loading';
import SearchBar from '@/components/common/SearchBar/SearchBar';
import SearchFilter from '@/components/common/SearchFilter/SearchFilter';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';
import Select from '@/components/common/Select/Select';

import ProductListSection from '@/components/Product/ProductListSection/ProductListSection';

import useDebounce from '@/hooks/useDebounce';
import useSearch from '@/hooks/useSearch';
import useUrlSyncState from '@/hooks/useUrlSyncState';

import { ENDPOINTS } from '@/constants/api';
import TITLE from '@/constants/header';
import { CATEGORIES } from '@/constants/product';
import SEARCH_PARAMS from '@/constants/searchParams';

import { SROnly } from '@/style/GlobalStyles';

type Option = { value: string; text: string };

type Sort = 'rating,desc' | 'reviewCount,desc';
type SortText = '평점 높은 순' | '리뷰 많은 순';

interface ProductSortOption extends Option {
  value: Sort;
  text: SortText;
}

const options: ProductSortOption[] = [
  { value: 'reviewCount,desc', text: '리뷰 많은 순' },
  { value: 'rating,desc', text: '평점 높은 순' },
];

const DefaultSort = options[1];

const PRODUCT_SEARCH_SIZE = 12;

function Products() {
  const [keyword, setKeyword] = useUrlSyncState(SEARCH_PARAMS.KEYWORD);
  const [category, setCategory] = useUrlSyncState<Category>(SEARCH_PARAMS.CATEGORY);
  const [sort, setSort] = useUrlSyncState<Sort>(SEARCH_PARAMS.SORT, DefaultSort.value);
  const [currDataLength, setCurrDataLength] = useState(0);
  const [loadedStateMessage, setLoadedStateMessage] = useState('');
  const debouncedKeyword = useDebounce<string>(keyword, 300);

  const {
    result: products,
    getNextPage,
    hasNextPage,
    isLoading,
    isReady,
    isError,
  } = useSearch<Product>({
    url: ENDPOINTS.PRODUCTS,
    size: String(PRODUCT_SEARCH_SIZE),
    query: debouncedKeyword,
    filter: {
      category,
      sort,
    },
  });

  const title = useMemo(
    () =>
      keyword === null
        ? category in CATEGORIES
          ? CATEGORIES[category]
          : TITLE.ALL_PRODUCT
        : `${keyword} 검색 결과`,
    [category, keyword]
  );

  const createLoadedMessage = (dataLength: number, prevDataLength: number) =>
    `총 ${dataLength - prevDataLength}개의 제품이 ${
      currDataLength !== 0 ? '추가로' : ''
    } 로딩되었습니다. ${
      currDataLength === 0 && hasNextPage
        ? '추가로 로딩할 수 있습니다. 마지막까지 탐색하면 자동으로 로딩됩니다.'
        : ''
    }`;

  useEffect(() => {
    if (isReady) {
      setLoadedStateMessage(createLoadedMessage(products.length, currDataLength));
      setCurrDataLength(products.length);
    }

    setTimeout(() => {
      setLoadedStateMessage('');
    }, 1000);
  }, [products]);

  useEffect(() => {
    setCurrDataLength(0);
  }, [sort, category, keyword]);

  return (
    <>
      <S.SearchBarWrapper aria-label={'제품 검색 영역'}>
        <SearchBar searchInput={keyword} setSearchInput={setKeyword} />
        <SearchFilter
          title={'카테고리'}
          value={category}
          setValue={setCategory}
          options={CATEGORIES}
        />
      </S.SearchBarWrapper>
      <SectionHeader title={title}>
        <Select value={sort} setValue={setSort} options={options} />
      </SectionHeader>
      <SROnly role={'status'}>{loadedStateMessage !== '' && loadedStateMessage}</SROnly>
      <AsyncWrapper fallback={<Loading />} isReady={isReady} isError={isError}>
        <ProductListSection
          title={title}
          data={products}
          getNextPage={getNextPage}
          isLoading={isLoading}
          isError={isError}
          pageSize={PRODUCT_SEARCH_SIZE}
        />
      </AsyncWrapper>
    </>
  );
}

export default Products;
