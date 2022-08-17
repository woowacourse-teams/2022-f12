import * as S from '@/components/DeskSetupCard/DeskSetupCard.style';

import { CATEGORIES } from '@/constants/product';

type Props = {
  item: Product;
  size: 's' | 'l';
  borderType: 'default' | 'selected' | 'selectedAnimation';
};

function DeskSetupCard({ size, item, borderType }: Props) {
  return (
    <S.Container size={size} borderType={borderType}>
      <S.ImageWrapper size={size}>
        <S.ProductImage
          src={
            item.imageUrl
              ? item.imageUrl
              : 'https://user-images.githubusercontent.com/64275588/185035487-a7832ec8-8290-457e-af6b-d6dc687a394d.svg'
          }
        />
      </S.ImageWrapper>
      <S.ProductTitleWrapper size={size}>
        <S.ProductTitle size={size}>
          {item.name
            ? item.name
            : `데스크 셋업에 등록한 ${CATEGORIES[item.category]}가 없어요`}
        </S.ProductTitle>
      </S.ProductTitleWrapper>
      <S.ReviewOpenButton size={size}>리뷰 보기</S.ReviewOpenButton>
    </S.Container>
  );
}

export default DeskSetupCard;
