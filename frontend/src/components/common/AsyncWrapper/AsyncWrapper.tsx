import React from 'react';

function AsyncWrapper({
  children,
  fallback,
  isReady,
}: React.PropsWithChildren<{ fallback: JSX.Element; isReady: boolean }>) {
  return isReady ? <>{children}</> : fallback;
}

export default AsyncWrapper;
