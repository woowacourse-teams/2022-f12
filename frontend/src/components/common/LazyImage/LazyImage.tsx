import { memo } from 'react';

import * as S from '@/components/common/LazyImage/LazyImage.style';

import useLazyImage from '@/hooks/useLazyImage';

type Props = {
  src: string;
};

function LazyImage({ src }: Props) {
  const { imageSrc, imageRef } = useLazyImage({ src });
  console.log(imageSrc);
  return <S.Image ref={imageRef} src={imageSrc} loading="lazy" />;
}
export default memo(LazyImage);
