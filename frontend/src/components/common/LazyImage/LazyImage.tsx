import { memo } from 'react';

import * as S from '@/components/common/LazyImage/LazyImage.style';

import useLazyImage from '@/hooks/useLazyImage';

type Props = {
  src: string;
  alt: string;
};

function LazyImage({ src, alt }: Props) {
  const { imageSrc, imageRef } = useLazyImage({ src });
  return <S.Image ref={imageRef} src={imageSrc} alt={alt} loading="lazy" />;
}
export default memo(LazyImage);
