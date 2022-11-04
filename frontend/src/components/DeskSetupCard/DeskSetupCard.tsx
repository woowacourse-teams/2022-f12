import * as S from '@/components/DeskSetupCard/DeskSetupCard.style';

import useGet from '@/hooks/api/useGet';
import useModal from '@/hooks/useModal';

import { ENDPOINTS } from '@/constants/api';
import { FAILURE_MESSAGES } from '@/constants/messages';
import ROUTES from '@/constants/routes';

type Props = {
  item: InventoryProduct;
  size: 's' | 'l';
  borderType: 'default' | 'selected' | 'selectedAnimation';
  isEditMode?: boolean;
  index?: number;
};

function DeskSetupCard({ size, item, borderType, isEditMode = false, index = 0 }: Props) {
  const { showReview } = useModal();

  const fetchData = useGet<InventoryReview>({
    url: `${ENDPOINTS.REVIEW_BY_INVENTORY_PRODUCT_ID(item.id)}`,
  });

  const handleReviewButtonClick = async () => {
    try {
      const review = await fetchData({});
      if (review === undefined) {
        throw new Error(FAILURE_MESSAGES.REVIEW_DELETED);
      }
      await showReview(review.content, review.rating, review.createdAt);
    } catch {
      return;
    }
  };

  return (
    <S.Container
      index={index}
      size={size}
      borderType={borderType}
      isEditMode={isEditMode}
    >
      <S.CustomLink to={`${ROUTES.PRODUCT}/${item?.product.id}`}>
        <S.ImageWrapper size={size}>
          <S.ProductImage src={item?.product.imageUrl} />
        </S.ImageWrapper>
        <S.ProductTitleWrapper size={size}>
          <S.ProductTitle size={size}>{item?.product.name}</S.ProductTitle>
        </S.ProductTitleWrapper>
      </S.CustomLink>
      <S.ReviewOpenButton size={size} onClick={handleReviewButtonClick}>
        리뷰 보기
      </S.ReviewOpenButton>
    </S.Container>
  );
}

export default DeskSetupCard;
