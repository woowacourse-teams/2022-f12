import { PropsWithChildren, useEffect, useMemo, useRef } from 'react';

import Loading from '@/components/common/Loading/Loading';

type Props = Omit<DataFetchStatus, 'isReady'> & {
  handleContentLoad: () => void;
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
    <section style={{ marginTop: '1rem' }} role={'feed'} aria-busy={!isLoading}>
      {children}
      {isLoading && <Loading />}
      <section
        aria-hidden={'true'}
        aria-label={'무한스크롤 목록 끝 지표'}
        tabIndex={0}
        ref={endRef}
        style={{ transform: 'translateY(-200px)' }}
      />
    </section>
  );
}

export default InfiniteScroll;
