import Loading from '@/components/common/Loading/Loading';
import { PropsWithChildren, useEffect, useMemo, useRef } from 'react';

type Props = {
  handleContentLoad: () => void;
  isLoading: boolean;
};

function InfiniteScroll({
  handleContentLoad,
  isLoading,
  children,
}: PropsWithChildren<Props>) {
  const endRef = useRef<HTMLDivElement>(null);

  const endOfContentObserver = useMemo(
    () =>
      new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting) {
          console.log(1);
          handleContentLoad();
        }
      }),
    []
  );

  useEffect(() => {
    if (!endRef.current) return;
    endOfContentObserver.observe(endRef.current);
  }, []);

  return (
    <>
      {children}
      {isLoading && <Loading />}
      <div ref={endRef} />
    </>
  );
}

export default InfiniteScroll;
