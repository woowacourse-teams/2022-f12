import { Link } from 'react-router-dom';

import * as S from '@/components/DeskSetupCard/DeskSetupCard.style';

import useGet from '@/hooks/api/useGet';
import useModal from '@/hooks/useModal';

import { ENDPOINTS } from '@/constants/api';
import ROUTES from '@/constants/routes';

type Props = {
  item: InventoryProduct;
  size: 's' | 'l';
  borderType: 'default' | 'selected' | 'selectedAnimation';
};

function DeskSetupCard({ size, item, borderType }: Props) {
  const { showReview } = useModal();

  const fetchData = useGet<InventoryReview>({
    url: `${ENDPOINTS.REVIEW_BY_INVENTORY_PRODUCT_ID(item.id)}`,
  });

  const handleReviewButtonClick = async () => {
    try {
      const review = await fetchData({});
      await showReview(review.content, review.rating, review.createdAt);
    } catch {
      return;
    }
  };

  return (
    <S.Container size={size} borderType={borderType}>
      <Link to={`${ROUTES.PRODUCT}/${item?.product.id}`}>
        <S.ImageWrapper size={size}>
          <S.ProductImage src={item?.product.imageUrl} />
        </S.ImageWrapper>
        <S.ProductTitleWrapper size={size}>
          <S.ProductTitle size={size}>{item?.product.name}</S.ProductTitle>
        </S.ProductTitleWrapper>
      </Link>
      <S.ReviewOpenButton size={size} onClick={handleReviewButtonClick}>
        리뷰 보기
      </S.ReviewOpenButton>
    </S.Container>
  );
}

export default DeskSetupCard;
