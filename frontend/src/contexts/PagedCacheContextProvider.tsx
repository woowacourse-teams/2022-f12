import { AxiosResponse } from 'axios';
import { createContext, PropsWithChildren, useState } from 'react';

import { CACHE_TIME } from '@/constants/cache';

type PagedCache = { cache: AxiosResponse[]; expires: number };
type PagedCacheMap = Map<string, PagedCache>;
type PagedCacheControllers = {
  getCache: (key: string) => PagedCache['cache'];
  addCache: (key: string, data: AxiosResponse) => void;
  removeCache: (key: string) => void;
};

export const PagedCacheControlContext = createContext<PagedCacheControllers>(null);

function PagedCacheContextProvider({ children }: PropsWithChildren) {
  const [cacheMap, setCacheMap] = useState<PagedCacheMap>(new Map());

  const getCache = (key: string) => {
    const target = cacheMap.get(key);
    if (target === undefined) return;

    const { cache, expires } = target;
    if (expires < Date.now()) return;

    return cache;
  };

  const addCache = (key: string, data: AxiosResponse) => {
    setCacheMap((prev) => {
      const newCache = new Map(prev);

      const target = prev.get(key) ? prev.get(key).cache : [];
      target.push(data);

      newCache.set(key, { cache: target, expires: Date.now() + CACHE_TIME.ONE_MINS });
      return newCache;
    });
  };

  const removeCache = (key: string) => {
    setCacheMap((prev) => {
      const newCache = new Map(prev);
      newCache.delete(key);
      return newCache;
    });
  };

  return (
    <PagedCacheControlContext.Provider value={{ getCache, addCache, removeCache }}>
      {children}
    </PagedCacheControlContext.Provider>
  );
}

export default PagedCacheContextProvider;
