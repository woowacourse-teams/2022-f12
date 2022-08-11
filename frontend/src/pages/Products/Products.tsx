import { useEffect, useMemo, useState } from 'react';

import * as S from '@/pages/Products/Products.style';
import ProductListSection from '@/components/ProductListSection/ProductListSection';
import Select from '@/components/common/Select/Select';
import { useLocation, useSearchParams } from 'react-router-dom';
import { CATEGORY } from '@/components/common/CategoryNav/CategoryNav';
import useSearch from '@/hooks/useSearch';
import { ENDPOINTS } from '@/constants/api';
import SearchBar from '@/components/common/SearchBar/SearchBar';
import SearchFilter from '@/components/SearchFilter/SearchFilter';

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
  const [searchParams, setSearchParams] = useSearchParams();
  const location = useLocation();

  const [sort, setSort] = useState<string>(
    // ,(쉼표)의 경우 param에 포함되면 encode, 상태로 사용하려면 decode 필요
    searchParams.get('sort')
      ? decodeURIComponent(searchParams.get('sort'))
      : DefaultSort.value
  );
  const [keyword, setKeyword] = useState<string>(searchParams.get('keyword'));
  const [category, setCategory] = useState<string>(
    searchParams.get('category')
  );

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
    () =>
      category in CATEGORY ? CATEGORY[category as keyof CATEGORY] : '모든 상품',
    [category]
  );

  // setState가 아니라 뒤로가기 등 상황에서 UI 동기화
  // 컴포넌트가 navigate 되지 않아 처리해주지 않으면 초기값 지정이 자동으로 되지 않음
  useEffect(() => {
    setCategory(searchParams.get('category'));
    setSort(searchParams.get('sort') || DefaultSort.value);
    setKeyword(searchParams.get('keyword'));
  }, [location.key]);

  const updateSearchParam = (
    key: string,
    value: string,
    defaultValue?: string
  ) => {
    // 초기 값이 null 아닌 경우 param에 포함되지 않을 수 있음
    // 이 때 다시 이 값을 param에 추가하고 재로딩 되는 것을 방지
    if (!searchParams.get(key) && value === defaultValue) return;

    if (searchParams.get(key) === value) return;

    if (value === null) {
      searchParams.delete(key);
    } else {
      searchParams.set(key, value);
    }

    setSearchParams(searchParams);
  };

  useEffect(() => {
    updateSearchParam('category', category);
  }, [category]);

  useEffect(() => {
    updateSearchParam('sort', sort, DefaultSort.value);
  }, [sort]);

  useEffect(() => {
    updateSearchParam('keyword', keyword);
  }, [keyword]);

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
      <ProductListSection
        title={title}
        data={!!products && products}
        getNextPage={getNextPage}
        addOn={<Select value={sort} setValue={setSort} options={options} />}
        isLoading={isLoading}
        isReady={isReady}
        isError={isError}
      />
    </>
  );
}

export default Products;
