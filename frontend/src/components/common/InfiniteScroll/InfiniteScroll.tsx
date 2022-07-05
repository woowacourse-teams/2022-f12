import { useEffect, useMemo, useRef } from 'react';

type Props = {
  handleContentLoad: () => void;
  children: React.ReactNode;
};

function InfiniteScroll({ handleContentLoad, children }: Props) {
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
    endOfContentObserver.observe(endRef.current);
  }, []);

  return (
    <>
      {children}
      <div ref={endRef} />
    </>
  );
}

export default InfiniteScroll;
