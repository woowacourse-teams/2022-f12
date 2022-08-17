import * as S from '@/components/DeskSetupCard/DeskSetupCard.style';

import useGetOne from '@/hooks/api/useGetOne';
import useModal from '@/hooks/useModal';

import { ENDPOINTS } from '@/constants/api';

type Props = {
  item: InventoryProduct;
  size: 's' | 'l';
  borderType: 'default' | 'selected' | 'selectedAnimation';
};

function DeskSetupCard({ size, item, borderType }: Props) {
  const { showReview } = useModal();

  const { data: review } = useGetOne<InventoryReview>({
    url: `${ENDPOINTS.REVIEW_BY_INVENTORY_PRODUCT_ID(String(item.id))}`,
  });

  const handleReviewButtonClick = async () => {
    try {
      await showReview(review.content, review.rating, review.createdAt);
    } catch {
      return;
    }
  };

  return (
    <S.Container size={size} borderType={borderType}>
      <S.ImageWrapper size={size}>
        <S.ProductImage src={item?.product?.imageUrl} />
      </S.ImageWrapper>
      <S.ProductTitleWrapper size={size}>
        <S.ProductTitle size={size}>{item?.product?.name}</S.ProductTitle>
      </S.ProductTitleWrapper>
      <S.ReviewOpenButton size={size} onClick={handleReviewButtonClick}>
        리뷰 보기
      </S.ReviewOpenButton>
    </S.Container>
  );
}

export default DeskSetupCard;
