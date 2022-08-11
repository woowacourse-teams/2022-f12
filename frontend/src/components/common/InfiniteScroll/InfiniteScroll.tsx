import { PropsWithChildren, useEffect, useMemo, useRef } from 'react';

import Loading from '@/components/common/Loading/Loading';

type Props = {
  handleContentLoad: () => void;
  isLoading: boolean;
  isError: boolean;
};

function InfiniteScroll({
  handleContentLoad,
  isLoading,
  isError,
  children,
}: PropsWithChildren<Props>) {
  const endRef = useRef<HTMLDivElement>(null);

  const endOfContentObserver = useMemo(
    () =>
      new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting) {
          handleContentLoad();
        }
      }),
    []
  );

  useEffect(() => {
    if (!endRef.current) return;
    if (isError) {
      endOfContentObserver.unobserve(endRef.current);
      return;
    }
    endOfContentObserver.observe(endRef.current);
  }, [isError]);

  return (
    <>
      {children}
      {isLoading && <Loading />}
      <section ref={endRef} aria-label="무한스크롤 목록 끝 지표" />
    </>
  );
}

export default InfiniteScroll;
