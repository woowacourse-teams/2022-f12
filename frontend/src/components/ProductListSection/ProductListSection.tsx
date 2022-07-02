import { Link } from 'react-router-dom';
import ProductCard from '../common/ProductCard/ProductCard';
import SectionHeader from '../common/SectionHeader/SectionHeader';

import * as S from './ProductListSection.style';

type Props = {
  data: { id: number; productImage: string; name: string; rating: number }[];
};

function ProductListSection({ data }: Props) {
  const productCardList = data.map(({ id, productImage, name, rating }) => (
    <Link to="/">
      <ProductCard
        key={id}
        productImage={productImage}
        name={name}
        rating={rating}
      />
    </Link>
  ));

  return (
    <S.Container>
      <SectionHeader>
        <S.Title>인기 있는 상품</S.Title>
        <S.CustomLink to="products"> +더보기</S.CustomLink>
      </SectionHeader>
      <S.Wrapper>{productCardList}</S.Wrapper>
    </S.Container>
  );
}

export default ProductListSection;
