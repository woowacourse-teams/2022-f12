import Rating from '@/components/common/Rating/Rating';
import ReviewCount from '@/components/common/ReviewCount/ReviewCount';

import * as S from '@/components/Product/ProductDetail/ProductDetail.style';

import { DANAWA_SEARCH_URL, GOOGLE_SEARCH_URL } from '@/constants/api';

type Props = {
  product: Product;
};

function ProductDetail({ product }: Props) {
  const { imageUrl, name, category, rating, reviewCount } = product;
  const isSoftware = category === 'software';
  const searchUrlBase = isSoftware ? GOOGLE_SEARCH_URL : DANAWA_SEARCH_URL;
  const searchUrl = `${searchUrlBase}${name}`;

  return (
    <S.Container>
      <S.Image src={imageUrl} aria-label="제품 이미지" />
      <S.Wrapper>
        <S.Name>{name}</S.Name>
        <S.SearchLink href={searchUrl} target={'_blank'} rel={'noopener noreferrer'}>
          {isSoftware ? '구글' : '다나와'}에서 검색하기
        </S.SearchLink>
        <S.Details>
          <ReviewCount reviewCount={reviewCount} size="large" />
          <Rating size="large" rating={rating} />
        </S.Details>
      </S.Wrapper>
    </S.Container>
  );
}

export default ProductDetail;
