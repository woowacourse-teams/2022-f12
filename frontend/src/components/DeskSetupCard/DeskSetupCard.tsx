import * as S from '@/components/DeskSetupCard/DeskSetupCard.style';

type Props = {
  item: Product;
  size: 's' | 'l';
  borderType: 'default' | 'selected' | 'selectedAnimation';
};

function DeskSetupCard({ size, item, borderType }: Props) {
  return (
    <S.Container size={size} borderType={borderType}>
      <S.ImageWrapper size={size}>
        <S.ProductImage src={item.imageUrl} />
      </S.ImageWrapper>
      <S.ProductTitleWrapper size={size}>
        <S.ProductTitle size={size}>{item.name}</S.ProductTitle>
      </S.ProductTitleWrapper>
      <S.ReviewOpenButton size={size}>리뷰 보기</S.ReviewOpenButton>
    </S.Container>
  );
}

export default DeskSetupCard;
