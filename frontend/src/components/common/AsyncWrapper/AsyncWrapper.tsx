import React from 'react';

import ErrorPlaceholder from '@/components/common/ErrorPlaceholder/ErrorPlaceholder';

type Props = {
  fallback: JSX.Element;
  isReady: DataFetchStatus['isReady'];
  isError?: DataFetchStatus['isError'];
};

function AsyncWrapper({
  children,
  fallback,
  isReady,
  isError,
}: React.PropsWithChildren<Props>) {
  const isLoadMoreError = isError && isReady; // 이미 받아온 데이터가 있으나 추가 로딩 시 오류가 발생하는 경우

  if (isError) return <ErrorPlaceholder />;
  if (isLoadMoreError)
    return (
      <>
        {children}
        <ErrorPlaceholder />
      </>
    );
  return isReady ? <>{children}</> : fallback;
}

export default AsyncWrapper;
