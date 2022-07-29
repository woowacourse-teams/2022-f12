import ErrorPlaceholder from '@/components/common/ErrorPlaceholder/ErrorPlaceholder';
import React from 'react';

type Props = {
  fallback: JSX.Element;
  isReady: boolean;
  isError?: boolean;
};

function AsyncWrapper({
  children,
  fallback,
  isReady,
  isError,
}: React.PropsWithChildren<Props>) {
  if (isError) return <ErrorPlaceholder />;
  return isReady ? <>{children}</> : fallback;
}

export default AsyncWrapper;
