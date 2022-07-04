import { useState } from 'react';

import Header from '../../components/common/Header/Header';
import ProductListSection from '../../components/ProductListSection/ProductListSection';
import Select from '../../components/common/Select/Select';
import { products } from './mockData';

import * as S from './Products.style';

const options = [
  { value: 'default', text: '기본 순' },
  { value: 'rating', text: '평점 높은 순' },
  { value: 'reviewCount', text: '리뷰 많은 순' },
];

function Products() {
  const [sorting, setSorting] = useState(options[1].value);

  return (
    <>
      <Header />
      <S.Main>
        <ProductListSection
          title={'인기 있는 상품'}
          data={products}
          addOn={
            <Select value={sorting} setValue={setSorting} options={options} />
          }
        />
      </S.Main>
    </>
  );
}

export default Products;
