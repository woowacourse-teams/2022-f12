import { useEffect, useRef, useState } from 'react';

type Props = {
  src: string;
};

function useLazyImage({ src }: Props) {
  const [imageSrc, setImageSrc] = useState<string | null>(null);
  const imageRef = useRef<HTMLImageElement>(null);

  useEffect(() => {
    let imageContentObserver: IntersectionObserver;

    if (imageRef && !imageSrc) {
      imageContentObserver = new IntersectionObserver(
        ([entry]) => {
          if (entry.isIntersecting) {
            setImageSrc(src);
            imageContentObserver.unobserve(imageRef.current);
          }
        },
        { threshold: [0.5] }
      );

      imageContentObserver.observe(imageRef.current);
    }

    return () => {
      imageContentObserver && imageContentObserver.disconnect();
    };
  }, [imageRef, imageSrc, src]);

  return { imageRef, imageSrc };
}

export default useLazyImage;
