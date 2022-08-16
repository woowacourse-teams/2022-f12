import { Link } from 'react-router-dom';

import * as S from '@/components/DeskSetupCard/DeskSetupCard.style';

import ROUTES from '@/constants/routes';

type Props = {
  inventoryId?: number;
  size: 's' | 'l';
};

function DeskSetupCard({ inventoryId, size }: Props) {
  return (
    <S.Container size={size}>
      <Link key={inventoryId} to={`${ROUTES.PRODUCT}/${inventoryId}`}>
        <S.ImageWrapper>
          <S.ProductImage src="https://img.danawa.com/prod_img/500000/337/103/img/10103337_1.jpg?shrink=330:330&_v=20220510143846" />
        </S.ImageWrapper>
        <S.ProductTitle size={size}>
          키보드키보드키보드키보드키보드키보드키보드키보드키보드키보드
        </S.ProductTitle>
        <S.ReviewOpenButton size={size}>리뷰 보기</S.ReviewOpenButton>
      </Link>
    </S.Container>
  );
}

export default DeskSetupCard;
