import { useEffect, useMemo, useState } from 'react';

import * as S from '@/pages/Products/Products.style';
import ProductListSection from '@/components/ProductListSection/ProductListSection';
import Select from '@/components/common/Select/Select';
import { useLocation, useNavigate, useSearchParams } from 'react-router-dom';
import { CATEGORY } from '@/components/common/CategoryNav/CategoryNav';
import useSearch from '@/hooks/useSearch';
import { ENDPOINTS } from '@/constants/api';
import SearchBar from '@/components/common/SearchBar/SearchBar';
import SearchFilter from '@/components/SearchFilter/SearchFilter';
import ROUTES from '@/constants/routes';

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
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const location = useLocation();

  const [sort, setSort] = useState<Sort>(DefaultSort.value);
  const [keyword, setKeyword] = useState<string>('');
  const [category, setCategory] = useState<string>(
    searchParams.get('category') || null
  );

  const {
    result: products,
    getNextPage,
    isLoading,
    isReady,
    isError,
  } = useSearch<Product>({
    url: ENDPOINTS.PRODUCTS,
    query: keyword !== '' ? keyword : null,
    size: '12',
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

  const handleCategoryFilterClick: React.MouseEventHandler<
    HTMLButtonElement
  > = (e) => {
    if (!(e.target instanceof HTMLButtonElement)) return;
    if (e.target.value === category) {
      navigate(ROUTES.PRODUCTS);
      return;
    }

    navigate(`${ROUTES.PRODUCTS}?category=${e.target.value}`);
  };

  useEffect(() => {
    setCategory(searchParams.get('category'));
  }, [location.key]);

  return (
    <>
      <S.SearchBarWrapper>
        <SearchBar searchInput={keyword} setSearchInput={setKeyword} />
        <SearchFilter
          title={'카테고리'}
          value={category}
          handleValueClick={handleCategoryFilterClick}
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
