import * as S from '@/components/common/WishButton/WishButton.style';

import BookmarkImage from '@/assets/bookmark.svg';
import BookmarkEmptyImage from '@/assets/bookmark_empty.svg';

type Prop = {
  added: boolean;
};

function WishButton({ added }: Prop) {
  return <S.Button>{added ? <BookmarkImage /> : <BookmarkEmptyImage />}</S.Button>;
}

export default WishButton;
